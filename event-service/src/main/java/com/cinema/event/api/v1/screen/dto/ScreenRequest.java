package com.cinema.event.api.v1.screen.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ScreenRequest(

        @NotBlank(message = "Screen name is required")
        @Size(max = 100, message = "Screen name must not exceed 100 characters")
        String name,

        @NotNull(message = "Total rows is required")
        @Positive(message = "Total rows must be positive")
        @Max(value = 26, message = "Total rows must not exceed 26")
        Integer totalRows,

        @NotNull(message = "Seats per row is required")
        @Positive(message = "Seats per row must be positive")
        @Max(value = 50, message = "Seats per row must not exceed 50")
        Integer seatsPerRow

) {
}
