package com.cinema.event.api.v1.event.exceptions;

import com.cinema.event.api.v1.common.exceptions.BusinessException;
import org.springframework.http.HttpStatus;

public class EventNotFoundException extends BusinessException {

    public EventNotFoundException(Long eventId) {
        super("EVENT_NOT_FOUND", "Event not found with ID: " + eventId, HttpStatus.NOT_FOUND);
    }
}
