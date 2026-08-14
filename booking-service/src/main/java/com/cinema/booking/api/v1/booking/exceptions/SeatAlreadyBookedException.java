package com.cinema.booking.api.v1.booking.exceptions;

import com.cinema.booking.api.v1.common.exceptions.BusinessException;
import org.springframework.http.HttpStatus;

import java.util.Collection;

public class SeatAlreadyBookedException extends BusinessException {

    public SeatAlreadyBookedException(Collection<Long> seatIds) {
        super(
                "SEAT_ALREADY_BOOKED",
                "Seats already booked for this show: " + seatIds,
                HttpStatus.CONFLICT
        );
    }

    public SeatAlreadyBookedException(Throwable cause) {
        super(
                "SEAT_ALREADY_BOOKED",
                "One or more selected seats were just booked by someone else",
                HttpStatus.CONFLICT,
                cause
        );
    }
}
