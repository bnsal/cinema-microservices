package com.cinema.booking.api.v1.reservation.repository;

import com.cinema.booking.api.v1.reservation.model.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import tools.jackson.databind.json.JsonMapper;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class ReservationCache {

    private static final String RESERVATION_KEY = "reservation:";
    private static final String SEAT_HOLD_KEY = "seat-hold:";

    private final StringRedisTemplate redisTemplate;
    private final JsonMapper jsonMapper;

    @Value("${booking.reservation.ttl-minutes}")
    private long ttlMinutes;

    public Duration ttl() {
        return Duration.ofMinutes(ttlMinutes);
    }

    public List<Long> holdSeats(Long showId, List<Long> seatIds, String reservationId) {
        List<Long> held = new ArrayList<>();
        List<Long> alreadyHeld = new ArrayList<>();

        for (Long seatId : seatIds) {
            String seatHoldKey = getSeatHoldKey(showId, seatId);
            Boolean acquired = redisTemplate.opsForValue()
                    .setIfAbsent(seatHoldKey, reservationId, ttl());
            if (Boolean.TRUE.equals(acquired)) {
                held.add(seatId);
            } else {
                alreadyHeld.add(seatId);
            }
        }

        if (!alreadyHeld.isEmpty()) {
            releaseSeats(showId, held);
        }
        return alreadyHeld;
    }

    public void releaseSeats(Long showId, Collection<Long> seatIds) {
        if (seatIds.isEmpty()) {
            return;
        }
        List<String> keys = seatIds.stream().map(seatId -> getSeatHoldKey(showId, seatId)).toList();
        redisTemplate.delete(keys);
    }

    public Set<Long> findHeldSeatIds(Long showId, List<Long> seatIds) {
        if (seatIds.isEmpty()) {
            return Set.of();
        }

        List<String> keys = seatIds.stream().map(seatId -> getSeatHoldKey(showId, seatId)).toList();
        List<String> holders = redisTemplate.opsForValue().multiGet(keys);
        if (holders == null) {
            return Set.of();
        }

        Set<Long> heldSeatIds = new HashSet<>();
        for (int index = 0; index < seatIds.size(); index++) {
            if (holders.get(index) != null) {
                heldSeatIds.add(seatIds.get(index));
            }
        }
        return heldSeatIds;
    }


    public void save(Reservation reservation) {
        redisTemplate.opsForValue()
                .set(getReservationKey(reservation.id()), jsonMapper.writeValueAsString(reservation), ttl());
    }

    public Optional<Reservation> find(String reservationId) {
        String json = redisTemplate.opsForValue().get(getReservationKey(reservationId));
        return Optional.ofNullable(json).map(value -> jsonMapper.readValue(value, Reservation.class));
    }

    public void delete(Reservation reservation) {
        redisTemplate.delete(getReservationKey(reservation.id()));
        releaseSeats(reservation.showId(), reservation.seatIds());
    }
    

    private String getReservationKey(String reservationId) {
        return RESERVATION_KEY + reservationId;
    }

    private String getSeatHoldKey(Long showId, Long seatId) {
        return SEAT_HOLD_KEY + showId + ":" + seatId;
    }
}
