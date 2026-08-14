# Movie Booking Platform

Two independently deployable Spring Boot microservices for booking movie show tickets.

## Architecture

```text
                          Client
                             |
              +--------------+--------------+
              |                             |
              v                             v
      Event Service :8081            Booking Service :8082 -----> Redis
              |                             |                  seat holds
          Event DB                      Booking DB             15 min TTL
              |                             |
          Theater                        Booking
              |                             |
           Screen                       BookedSeat
              |
            Seat
              |
          MovieShow  <---- reads show + seat layout ----+
```

**Event Service** owns the catalogue and the physical world: theaters, their screens, the seats
in each screen, the events that can be staged, and the movie shows scheduled on a screen.

**Booking Service** owns everything about a sale: which seats of a show are held, which are
sold, what a booking cost, and which discounts were applied. It reads the show and its seat
layout from the Event Service over HTTP and never touches its database.

Each service owns its own database. There are no shared tables, no cross-service joins and no
distributed transactions. The two services share no code either, so neither can be recompiled
into the other by accident; the small API response envelope is deliberately duplicated.

## Domain model

An **Event** is anything that can be staged, and its `type` says what kind it is: `MOVIE`,
`CONCERT` or `PLAY`. A movie is therefore a type of event rather than a separate entity, so the
catalogue can grow without a new table. A **MovieShow** schedules a `MOVIE` event on a screen at
a date and time; scheduling any other event type is rejected.

A **Screen** declares its layout as `totalRows` x `seatsPerRow`. Seats are generated from that
blueprint in one call, producing `A1..A10` through `E1..E10` for a five-by-ten screen. Seats are
physical positions only. They carry no availability flag, because availability is per show and
belongs to the Booking Service.

## Booking a ticket: reserve, then pay

Booking is two steps, because a user needs time to pay and their seats must not be sold to
someone else while they do.

```text
POST /reservations          seats held in Redis for 15 minutes, price returned
      |
      |  user pays within 15 minutes            user never pays
      v                                                |
POST /bookings                                         v
  payment -> booking row -> holds deleted        keys expire on their own,
  -> notification sent                           seats are available again
```

**Reserving** takes an exclusive hold on each seat and prices the tickets, so the amount is
agreed before any money moves. Two keys are written to Redis, both with the same TTL:

```text
seat-hold:{showId}:{seatId} -> reservationId    the exclusive hold on one seat
reservation:{reservationId} -> JSON             the seats and the agreed price
```

Each hold is taken with a single `SET NX EX` command, so two users racing for the same seat can
never both win. If any seat in the selection is already held, the holds taken so far in that
request are released and the whole reservation fails. Nothing is ever half-reserved.

**Confirming** loads the reservation from Redis, charges the payment, writes the booking, then
deletes the reservation and its holds and sends the notification. If the reservation is gone the
confirmation returns `410 RESERVATION_EXPIRED`, because a reservation that expired and one that
never existed are indistinguishable, and both mean the same thing to the caller.

**Expiry needs no scheduled job.** Every key carries the TTL, so an abandoned checkout cleans
itself up. The TTL is configured with `booking.reservation.ttl-minutes`.

## Preventing double booking

Three layers, each catching what the one before it cannot:

1. **The Redis hold** stops a second user from reserving a seat that is being paid for.
2. **The database check** at confirmation stops a seat that was sold in an earlier session from
   being sold again. This is not merely defensive: holds are written before the reservation
   payload, so they expire a moment earlier, and another user can reserve in that window.
3. **The unique constraint** on `booked_seats (show_id, seat_id)` is the real guarantee. Two
   concurrent confirmations can pass the check above; only one can pass the constraint. The
   resulting `DataIntegrityViolationException` becomes `409 SEAT_ALREADY_BOOKED`.

The cache is checked before the database when reserving, because a live hold is the common
reason a seat is unavailable.

## Discount strategy

Ticket pricing lives in `booking-service`, in the `booking.discount` package, and runs at
reservation time so the user sees the amount before paying:

| Class | Rule |
|---|---|
| `AfternoonShowDiscountStrategy` | 20% off every ticket of a show starting between 12:00 and 16:00 |
| `ThirdTicketDiscountStrategy` | 50% off the third ticket in a booking |

`DiscountEngine` receives every `DiscountStrategy` bean through constructor injection and returns
the **best single discount** a ticket qualifies for. Discounts do not stack. A ticket is priced
individually, because a rule can depend on the ticket's position within the booking.

Adding an offer means adding one `@Component` that implements `DiscountStrategy`. No existing
class changes.

Four seats on a 14:00 show priced at 200:

```text
ticket 1  ->  afternoon 20%             ->  160
ticket 2  ->  afternoon 20%             ->  160
ticket 3  ->  best of 20% and 50%       ->  100
ticket 4  ->  afternoon 20%             ->  160
                                            ---
total 800      discount 220      final    580
```

## Resilience

`ShowGateway` wraps the Feign client so the rest of the Booking Service only sees domain
exceptions. A `404` from the Event Service means the show does not exist and becomes
`404 SHOW_NOT_FOUND`. Connection failures and timeouts become `503 EVENT_SERVICE_UNAVAILABLE`.
A Resilience4j circuit breaker stops calling an Event Service that is already failing; a missing
show is configured as an ignored exception so it never trips the breaker.

## APIs

### Event Service (`:8081`)

```text
POST   /api/v1/theaters
GET    /api/v1/theaters?city={city}
GET    /api/v1/theaters/{theaterId}

POST   /api/v1/theaters/{theaterId}/screens
GET    /api/v1/theaters/{theaterId}/screens
GET    /api/v1/theaters/{theaterId}/screens/{screenId}

POST   /api/v1/screens/{screenId}/seats          generates the screen's seat layout
GET    /api/v1/screens/{screenId}/seats

POST   /api/v1/events
GET    /api/v1/events?type={MOVIE|CONCERT|PLAY}
GET    /api/v1/events/{eventId}

POST   /api/v1/movie-shows
GET    /api/v1/movie-shows?eventId={id}&date={yyyy-MM-dd}
GET    /api/v1/movie-shows/{showId}
GET    /api/v1/movie-shows/{showId}/seats
```

### Booking Service (`:8082`)

```text
GET    /api/v1/shows/{showId}/seats              every seat as AVAILABLE, RESERVED or BOOKED

POST   /api/v1/reservations                      holds seats for 15 minutes, returns the price
GET    /api/v1/reservations/{reservationId}      a live hold and when it expires

POST   /api/v1/bookings                          pays for a reservation and confirms it
GET    /api/v1/bookings/{bookingId}
GET    /api/v1/bookings?userId={userId}
```

Every response uses the same envelope:

```json
{ "success": true, "message": "Seats reserved successfully", "data": { } }
{ "success": false, "code": "SEAT_ALREADY_RESERVED", "message": "Seats are currently held by another user: [3, 4]" }
```

## Validation rules

| Rule | Response |
|---|---|
| A base price is a whole number; a fractional value is rejected, never truncated | `400 MALFORMED_REQUEST` |
| A screen name is unique within its theater | `409 SCREEN_ALREADY_EXISTS` |
| Seats can be generated for a screen only once | `409 SEATS_ALREADY_GENERATED` |
| Only a `MOVIE` event can be scheduled as a movie show | `400 EVENT_IS_NOT_A_MOVIE` |
| A screen runs one show at a time, including shows that cross midnight | `409 SHOW_TIME_CONFLICT` |
| Reserved seats must belong to the show's screen | `400 INVALID_SEAT_SELECTION` |
| The same seat cannot appear twice in one reservation | `400 INVALID_SEAT_SELECTION` |
| A seat being paid for cannot be reserved by anyone else | `409 SEAT_ALREADY_RESERVED` |
| A seat can be sold once per show | `409 SEAT_ALREADY_BOOKED` |
| Payment must happen before the reservation expires | `410 RESERVATION_EXPIRED` |

## Build and run

Java 21 and a running Redis are required.

```bash
redis-server                                  # or: brew services start redis
./mvnw clean install

./mvnw -pl event-service spring-boot:run      # http://localhost:8081
./mvnw -pl booking-service spring-boot:run    # http://localhost:8082
```

Swagger UI: `http://localhost:8081/swagger-ui.html` and `http://localhost:8082/swagger-ui.html`.

Each service writes its own file-based H2 database under `data/`. Reservations are the only
state kept in Redis, and they are meant to be lost when they expire.

## Walk-through

```bash
# a theater with one 3x4 screen
curl -X POST localhost:8081/api/v1/theaters -H 'Content-Type: application/json' \
  -d '{"name":"PVR Forum Mall","city":"Bengaluru","address":"Koramangala"}'

curl -X POST localhost:8081/api/v1/theaters/1/screens -H 'Content-Type: application/json' \
  -d '{"name":"Audi 1","totalRows":3,"seatsPerRow":4}'

curl -X POST localhost:8081/api/v1/screens/1/seats

# a movie, scheduled at 14:00
curl -X POST localhost:8081/api/v1/events -H 'Content-Type: application/json' \
  -d '{"title":"Inception","type":"MOVIE","language":"English","durationMinutes":148}'

curl -X POST localhost:8081/api/v1/movie-shows -H 'Content-Type: application/json' \
  -d '{"eventId":1,"screenId":1,"showDate":"2026-12-01","startTime":"14:00:00","basePrice":200}'

# what is free
curl "localhost:8082/api/v1/shows/1/seats"

# hold four seats for 15 minutes; the response carries the price and the reservation id
curl -X POST localhost:8082/api/v1/reservations -H 'Content-Type: application/json' \
  -d '{"userId":1,"showId":1,"seatIds":[1,2,3,4]}'

# the holds are visible in Redis with their remaining time
redis-cli KEYS 'seat-hold:1:*'
redis-cli TTL 'seat-hold:1:1'

# pay within the window to turn the hold into a booking
curl -X POST localhost:8082/api/v1/bookings -H 'Content-Type: application/json' \
  -d '{"reservationId":"<reservationId from the reservation response>"}'
```

To watch a reservation expire without waiting fifteen minutes, lower
`booking.reservation.ttl-minutes`, or delete its keys with `redis-cli DEL` and confirm again.

## Package structure

Both services follow the same layering, one package per domain:

```text
api/v1/<domain>/
    <Domain>Controller.java     HTTP layer, no business logic
    dto/                        request and response records
    entities/                   JPA entities
    repository/                 Spring Data repositories
    mapper/                     entity <-> DTO conversion
    service/                    interface
    service/impl/               business rules
    exceptions/                 one exception per rule, each carrying its HTTP status
api/v1/common/                  response envelope, error handling, OpenAPI
```

The Booking Service adds `reservation/` for the Redis-backed hold, and two small support
packages, `payment/` and `notification/`, that are interfaces with one implementation each.

Every domain exception extends `BusinessException`, which carries an error code and an HTTP
status. A single `@RestControllerAdvice` turns them into the error envelope, so no controller
contains a try/catch or an if/else on failure.

## Scope

Kept deliberately out to hold the codebase to what the brief asked for:

- Authentication. `userId` arrives in the request; in production it would come from the token.
- A real payment gateway. `SimulatedPaymentService` returns a reference and always succeeds; a
  real one would call the provider and fail the booking when the charge is declined.
- A real notification channel. `LoggingNotificationService` logs the confirmation; a real one
  would publish an event for an email or SMS service to consume.
- Refunding a payment whose booking then failed to save. Payment happens before the insert, so a
  crash in between leaves a charge with no booking. Production needs a saga or an outbox here.
- Releasing a reservation early when a user abandons checkout. The TTL covers it, a `DELETE`
  endpoint would just make it immediate.
- Update and delete endpoints on the catalogue. Reference data is created and read here.
- Seat categories and per-category pricing. Pricing is the base price plus the discount rules.
- Service discovery and an API gateway. Service URLs are configuration.

## Technologies

- Java 21, Spring Boot, Maven multi-module build
- Spring Data JPA, Hibernate, H2
- Spring Data Redis for reservation holds with a TTL
- Bean Validation
- Spring Cloud OpenFeign and Resilience4j in the Booking Service
- Swagger / OpenAPI
