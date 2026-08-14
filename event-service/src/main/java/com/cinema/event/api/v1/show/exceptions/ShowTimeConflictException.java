package com.cinema.event.api.v1.show.exceptions;

import com.cinema.event.api.v1.common.exceptions.BusinessException;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalTime;

public class ShowTimeConflictException extends BusinessException {

    public ShowTimeConflictException(Long screenId, LocalDate showDate, LocalTime startTime) {
        super(
                "SHOW_TIME_CONFLICT",
                "Screen " + screenId + " already has an overlapping show on " + showDate + " at " + startTime,
                HttpStatus.CONFLICT
        );
    }
}
