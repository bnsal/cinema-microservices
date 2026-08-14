package com.cinema.booking.api.v1.show.client.exceptions;

import com.cinema.booking.api.v1.common.exceptions.BusinessException;
import org.springframework.http.HttpStatus;

public class EventServiceUnavailableException extends BusinessException {

    public EventServiceUnavailableException(Throwable cause) {
        super(
                "EVENT_SERVICE_UNAVAILABLE",
                "Event service is currently unavailable",
                HttpStatus.SERVICE_UNAVAILABLE,
                cause
        );
    }
}
