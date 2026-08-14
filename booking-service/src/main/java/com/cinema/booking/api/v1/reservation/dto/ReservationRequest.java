package com.cinema.booking.api.v1.reservation.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record ReservationRequest(

        @NotNull(message = "User ID is required")
        @Positive(message = "User ID must be positive")
        Long userId,

        @NotNull(message = "Show ID is required")
        @Positive(message = "Show ID must be positive")
        Long showId,

        @NotEmpty(message = "At least one seat must be selected")
        List<Long> seatIds

) {
}
