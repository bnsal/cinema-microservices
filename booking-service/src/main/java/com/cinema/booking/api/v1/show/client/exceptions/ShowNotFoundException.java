package com.cinema.booking.api.v1.show.client.exceptions;

import com.cinema.booking.api.v1.common.exceptions.BusinessException;
import org.springframework.http.HttpStatus;

public class ShowNotFoundException extends BusinessException {

    public ShowNotFoundException(Long showId) {
        super("SHOW_NOT_FOUND", "Movie show not found with ID: " + showId, HttpStatus.NOT_FOUND);
    }
}
