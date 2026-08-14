package com.cinema.booking.api.v1.notification;

import com.cinema.booking.api.v1.booking.entities.Booking;

public interface NotificationService {

    void bookingConfirmed(Booking booking);
}
