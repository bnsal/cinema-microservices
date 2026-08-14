package com.cinema.event.api.v1.screen.exceptions;

import com.cinema.event.api.v1.common.exceptions.BusinessException;
import org.springframework.http.HttpStatus;

public class ScreenNotFoundException extends BusinessException {

    public ScreenNotFoundException(Long screenId) {
        super("SCREEN_NOT_FOUND", "Screen not found with ID: " + screenId, HttpStatus.NOT_FOUND);
    }
}
