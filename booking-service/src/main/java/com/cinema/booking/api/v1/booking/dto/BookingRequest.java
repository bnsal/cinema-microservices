package com.cinema.booking.api.v1.booking.dto;

import jakarta.validation.constraints.NotBlank;

public record BookingRequest(

        @NotBlank(message = "Reservation ID is required")
        String reservationId

) {
}
