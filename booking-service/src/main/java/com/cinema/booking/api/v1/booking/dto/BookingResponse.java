package com.cinema.booking.api.v1.booking.dto;

import java.util.List;

public record BookingResponse(
        Long id,

        Long userId,
        Long showId,
        List<Long> seatIds,

        Integer totalAmount,
        Integer discountAmount,
        Integer finalAmount,

        String paymentReference
) {
}
