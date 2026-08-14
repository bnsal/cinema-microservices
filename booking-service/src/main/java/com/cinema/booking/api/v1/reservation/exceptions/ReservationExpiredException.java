package com.cinema.booking.api.v1.reservation.exceptions;

import com.cinema.booking.api.v1.common.exceptions.BusinessException;
import org.springframework.http.HttpStatus;

public class ReservationExpiredException extends BusinessException {

    public ReservationExpiredException(String reservationId) {
        super(
                "RESERVATION_EXPIRED",
                "Reservation has expired or does not exist: " + reservationId,
                HttpStatus.GONE
        );
    }
}
