package com.cinema.event.api.v1.theater.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TheaterRequest(

        @NotBlank(message = "Theater name is required")
        @Size(max = 150, message = "Theater name must not exceed 150 characters")
        String name,

        @NotBlank(message = "City is required")
        @Size(max = 100, message = "City must not exceed 100 characters")
        String city,

        @NotBlank(message = "Address is required")
        @Size(max = 250, message = "Address must not exceed 250 characters")
        String address

) {
}
