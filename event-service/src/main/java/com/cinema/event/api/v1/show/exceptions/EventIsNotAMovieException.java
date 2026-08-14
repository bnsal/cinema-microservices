package com.cinema.event.api.v1.show.exceptions;

import com.cinema.event.api.v1.common.exceptions.BusinessException;
import com.cinema.event.api.v1.event.entities.EventType;
import org.springframework.http.HttpStatus;

public class EventIsNotAMovieException extends BusinessException {

    public EventIsNotAMovieException(Long eventId, EventType type) {
        super(
                "EVENT_IS_NOT_A_MOVIE",
                "Event with ID " + eventId + " is of type " + type + " and cannot be scheduled as a movie show",
                HttpStatus.BAD_REQUEST
        );
    }
}
