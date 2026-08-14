package com.cinema.event.api.v1.show.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.time.LocalTime;

public record MovieShowRequest(

        @NotNull(message = "Event ID is required")
        @Positive(message = "Event ID must be positive")
        Long eventId,

        @NotNull(message = "Screen ID is required")
        @Positive(message = "Screen ID must be positive")
        Long screenId,

        @NotNull(message = "Show date is required")
        @FutureOrPresent(message = "Show date must be today or in the future")
        LocalDate showDate,

        @NotNull(message = "Start time is required")
        LocalTime startTime,

        @NotNull(message = "Base price is required")
        @Positive(message = "Base price must be positive")
        Integer basePrice

) {
}
