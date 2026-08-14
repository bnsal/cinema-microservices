package com.cinema.booking.api.v1.booking.exceptions;

import com.cinema.booking.api.v1.common.exceptions.BusinessException;
import org.springframework.http.HttpStatus;

public class BookingNotFoundException extends BusinessException {

    public BookingNotFoundException(Long bookingId) {
        super("BOOKING_NOT_FOUND", "Booking not found with ID: " + bookingId, HttpStatus.NOT_FOUND);
    }
}
