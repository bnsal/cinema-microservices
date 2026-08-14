package com.cinema.event.api.v1.event.dto;

import com.cinema.event.api.v1.event.entities.EventType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record EventRequest(

        @NotBlank(message = "Title is required")
        @Size(max = 200, message = "Title must not exceed 200 characters")
        String title,

        @NotNull(message = "Event type is required")
        EventType type,

        @NotBlank(message = "Language is required")
        @Size(max = 50, message = "Language must not exceed 50 characters")
        String language,

        @NotNull(message = "Duration is required")
        @Positive(message = "Duration must be positive")
        @Max(value = 600, message = "Duration must not exceed 600 minutes")
        Integer durationMinutes

) {
}
