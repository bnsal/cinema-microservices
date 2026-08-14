package com.cinema.event.api.v1.seat.exceptions;

import com.cinema.event.api.v1.common.exceptions.BusinessException;
import org.springframework.http.HttpStatus;

public class SeatsAlreadyGeneratedException extends BusinessException {

    public SeatsAlreadyGeneratedException(Long screenId) {
        super(
                "SEATS_ALREADY_GENERATED",
                "Seats are already generated for screen with ID: " + screenId,
                HttpStatus.CONFLICT
        );
    }
}
