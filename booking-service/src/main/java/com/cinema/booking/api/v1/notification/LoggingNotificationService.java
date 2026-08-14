package com.cinema.booking.api.v1.notification;

import com.cinema.booking.api.v1.booking.entities.Booking;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoggingNotificationService implements NotificationService {

    @Override
    public void bookingConfirmed(Booking booking) {
        log.info("Booking {} confirmed for user {}: {} seat(s) on show {}, amount paid {}, payment reference {}",
                booking.getId(),
                booking.getUserId(),
                booking.getBookedSeats().size(),
                booking.getShowId(),
                booking.getFinalAmount(),
                booking.getPaymentReference());
    }
}
