package com.cinema.event.api.v1.show.exceptions;

import com.cinema.event.api.v1.common.exceptions.BusinessException;
import org.springframework.http.HttpStatus;

public class MovieShowNotFoundException extends BusinessException {

    public MovieShowNotFoundException(Long showId) {
        super("MOVIE_SHOW_NOT_FOUND", "Movie show not found with ID: " + showId, HttpStatus.NOT_FOUND);
    }
}
