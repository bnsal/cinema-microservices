package com.cinema.booking.api.v1.reservation.dto;

import java.time.Instant;
import java.util.List;

public record ReservationResponse(
        String reservationId,

        Long userId,
        Long showId,
        List<Long> seatIds,

        Integer totalAmount,
        Integer discountAmount,
        Integer finalAmount,

        Instant expiresAt
) {
}
