package com.cinema.event.api.v1.theater.dto;

public record TheaterResponse(
        Long id,
        String name,
        String city,
        String address
) {
}
