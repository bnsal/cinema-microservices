package com.cinema.event.api.v1.show.mapper;

import com.cinema.event.api.v1.event.entities.Event;
import com.cinema.event.api.v1.screen.entities.Screen;
import com.cinema.event.api.v1.show.dto.MovieShowRequest;
import com.cinema.event.api.v1.show.dto.MovieShowResponse;
import com.cinema.event.api.v1.show.entities.MovieShow;
import org.springframework.stereotype.Component;

@Component
public class MovieShowMapper {

    public MovieShow toEntity(MovieShowRequest request, Event event, Screen screen) {
        MovieShow movieShow = new MovieShow();
        movieShow.setEvent(event);
        movieShow.setScreen(screen);
        movieShow.setShowDate(request.showDate());
        movieShow.setStartTime(request.startTime());
        movieShow.setBasePrice(request.basePrice());
        return movieShow;
    }

    public MovieShowResponse toResponse(MovieShow movieShow) {
        Event event = movieShow.getEvent();
        Screen screen = movieShow.getScreen();
        return new MovieShowResponse(
                movieShow.getId(),
                event.getId(),
                event.getTitle(),
                screen.getTheater().getId(),
                screen.getTheater().getName(),
                screen.getId(),
                screen.getName(),
                movieShow.getShowDate(),
                movieShow.getStartTime(),
                event.getDurationMinutes(),
                movieShow.getBasePrice()
        );
    }
}
