package com.cinema.booking.api.v1.reservation.service.impl;

import com.cinema.booking.api.v1.booking.discount.DiscountEngine;
import com.cinema.booking.api.v1.booking.discount.TicketContext;
import com.cinema.booking.api.v1.booking.entities.BookedSeat;
import com.cinema.booking.api.v1.booking.exceptions.SeatAlreadyBookedException;
import com.cinema.booking.api.v1.booking.repository.BookedSeatRepository;
import com.cinema.booking.api.v1.reservation.dto.ReservationRequest;
import com.cinema.booking.api.v1.reservation.dto.ReservationResponse;
import com.cinema.booking.api.v1.reservation.exceptions.InvalidSeatSelectionException;
import com.cinema.booking.api.v1.reservation.exceptions.ReservationExpiredException;
import com.cinema.booking.api.v1.reservation.exceptions.SeatAlreadyReservedException;
import com.cinema.booking.api.v1.reservation.mapper.ReservationMapper;
import com.cinema.booking.api.v1.reservation.model.Reservation;
import com.cinema.booking.api.v1.reservation.repository.ReservationCache;
import com.cinema.booking.api.v1.reservation.service.ReservationService;
import com.cinema.booking.api.v1.show.client.dto.MovieShowClientResponse;
import com.cinema.booking.api.v1.show.client.dto.SeatClientResponse;
import com.cinema.booking.api.v1.show.client.gateway.ShowGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationCache reservationCache;
    private final BookedSeatRepository bookedSeatRepository;
    private final ReservationMapper reservationMapper;
    private final ShowGateway showGateway;
    private final DiscountEngine discountEngine;

    @Override
    public ReservationResponse reserve(ReservationRequest request) {
        MovieShowClientResponse show = showGateway.findShow(request.showId());

        String reservationId = UUID.randomUUID().toString();
        List<Long> alreadyHeld = reservationCache.holdSeats(request.showId(), request.seatIds(), reservationId);
        if (!alreadyHeld.isEmpty()) {
            throw new SeatAlreadyReservedException(alreadyHeld);
        }

        //reassuring from db if its not already booked
        List<Long> alreadyBooked = findBookedSeatIds(request.showId(), request.seatIds());
        if (!alreadyBooked.isEmpty()) {
            reservationCache.releaseSeats(request.showId(), request.seatIds());
            throw new SeatAlreadyBookedException(alreadyBooked);
        }

        Reservation reservation = priceReservation(reservationId, request, show);
        reservationCache.save(reservation);

        return reservationMapper.toResponse(reservation);
    }
    

    @Override
    public ReservationResponse getById(String reservationId) {
        Reservation reservation = reservationCache.find(reservationId)
                .orElseThrow(() -> new ReservationExpiredException(reservationId));
        return reservationMapper.toResponse(reservation);
    }


    private Reservation priceReservation(
            String reservationId, ReservationRequest request, MovieShowClientResponse show) {
        int basePrice = show.basePrice();
        int finalAmount = 0;

        for (int position = 1; position <= request.seatIds().size(); position++) {
            TicketContext context = new TicketContext(position, show.startTime());
            int discountPercentage = discountEngine.bestDiscountPercentageFor(context);
            finalAmount += priceAfterDiscount(basePrice, discountPercentage);
        }

        int totalAmount = basePrice * request.seatIds().size();

        return new Reservation(
                reservationId,
                request.userId(),
                request.showId(),
                request.seatIds(),
                totalAmount,
                totalAmount - finalAmount,
                finalAmount,
                Instant.now().plus(reservationCache.ttl())
        );
    }

    private int priceAfterDiscount(int basePrice, int discountPercentage) {
        return Math.round(basePrice * (100 - discountPercentage) / 100f);
    }

    private List<Long> findBookedSeatIds(Long showId, List<Long> seatIds) {
        return bookedSeatRepository.findByShowIdAndSeatIdIn(showId, seatIds).stream()
                .map(BookedSeat::getSeatId)
                .toList();
    }
}
