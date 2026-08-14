package com.cinema.event.api.v1.theater.exceptions;

import com.cinema.event.api.v1.common.exceptions.BusinessException;
import org.springframework.http.HttpStatus;

public class TheaterNotFoundException extends BusinessException {

    public TheaterNotFoundException(Long theaterId) {
        super("THEATER_NOT_FOUND", "Theater not found with ID: " + theaterId, HttpStatus.NOT_FOUND);
    }
}
