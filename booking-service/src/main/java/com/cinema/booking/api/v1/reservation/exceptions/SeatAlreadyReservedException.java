package com.cinema.booking.api.v1.reservation.exceptions;

import com.cinema.booking.api.v1.common.exceptions.BusinessException;
import org.springframework.http.HttpStatus;

import java.util.Collection;

public class SeatAlreadyReservedException extends BusinessException {

    public SeatAlreadyReservedException(Collection<Long> seatIds) {
        super(
                "SEAT_ALREADY_RESERVED",
                "Seats are currently held by another user: " + seatIds,
                HttpStatus.CONFLICT
        );
    }
}
