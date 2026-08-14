package com.cinema.booking.api.v1.reservation.model;

import java.time.Instant;
import java.util.List;

/**
 * A temporary hold on seats
 */
public record Reservation(
        String id,
        Long userId,
        Long showId,
        List<Long> seatIds,
        Integer totalAmount,
        Integer discountAmount,
        Integer finalAmount,
        Instant expiresAt
) {
}
