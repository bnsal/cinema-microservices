package com.cinema.event.api.v1.screen.mapper;

import com.cinema.event.api.v1.screen.dto.ScreenRequest;
import com.cinema.event.api.v1.screen.dto.ScreenResponse;
import com.cinema.event.api.v1.screen.entities.Screen;
import com.cinema.event.api.v1.theater.entities.Theater;
import org.springframework.stereotype.Component;

@Component
public class ScreenMapper {

    public Screen toEntity(ScreenRequest request, Theater theater) {
        Screen screen = new Screen();
        screen.setTheater(theater);
        screen.setName(request.name());
        screen.setTotalRows(request.totalRows());
        screen.setSeatsPerRow(request.seatsPerRow());
        return screen;
    }

    public ScreenResponse toResponse(Screen screen) {
        return new ScreenResponse(
                screen.getId(),
                screen.getTheater().getId(),
                screen.getName(),
                screen.getTotalRows(),
                screen.getSeatsPerRow()
        );
    }
}
