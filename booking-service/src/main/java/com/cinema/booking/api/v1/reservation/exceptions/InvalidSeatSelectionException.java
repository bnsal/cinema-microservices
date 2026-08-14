package com.cinema.booking.api.v1.reservation.exceptions;

import com.cinema.booking.api.v1.common.exceptions.BusinessException;
import org.springframework.http.HttpStatus;

public class InvalidSeatSelectionException extends BusinessException {

    public InvalidSeatSelectionException(String message) {
        super("INVALID_SEAT_SELECTION", message, HttpStatus.BAD_REQUEST);
    }
}
