package com.cinema.event.api.v1.event.dto;

import com.cinema.event.api.v1.event.entities.EventType;

public record EventResponse(
        Long id,
        String title,
        EventType type,
        String language,
        Integer durationMinutes
) {
}
