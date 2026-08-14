package com.cinema.event.api.v1.screen.exceptions;

import com.cinema.event.api.v1.common.exceptions.BusinessException;
import org.springframework.http.HttpStatus;

public class ScreenAlreadyExistsException extends BusinessException {

    public ScreenAlreadyExistsException(Long theaterId, String name) {
        super(
                "SCREEN_ALREADY_EXISTS",
                "Screen '" + name + "' already exists in theater with ID: " + theaterId,
                HttpStatus.CONFLICT
        );
    }
}
