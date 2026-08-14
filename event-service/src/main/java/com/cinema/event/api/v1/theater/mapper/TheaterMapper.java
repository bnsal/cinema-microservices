package com.cinema.event.api.v1.theater.mapper;

import com.cinema.event.api.v1.theater.dto.TheaterRequest;
import com.cinema.event.api.v1.theater.dto.TheaterResponse;
import com.cinema.event.api.v1.theater.entities.Theater;
import org.springframework.stereotype.Component;

@Component
public class TheaterMapper {

    public Theater toEntity(TheaterRequest request) {
        Theater theater = new Theater();
        theater.setName(request.name());
        theater.setCity(request.city());
        theater.setAddress(request.address());
        return theater;
    }

    public TheaterResponse toResponse(Theater theater) {
        return new TheaterResponse(
                theater.getId(),
                theater.getName(),
                theater.getCity(),
                theater.getAddress()
        );
    }
}
