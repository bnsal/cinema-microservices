package com.cinema.event.api.v1.screen.dto;

public record ScreenResponse(
        Long id,
        Long theaterId,
        String name,
        Integer totalRows,
        Integer seatsPerRow
) {
}
