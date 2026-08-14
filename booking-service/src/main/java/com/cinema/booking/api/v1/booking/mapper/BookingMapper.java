package com.cinema.booking.api.v1.booking.mapper;

import com.cinema.booking.api.v1.booking.dto.BookingResponse;
import com.cinema.booking.api.v1.booking.entities.BookedSeat;
import com.cinema.booking.api.v1.booking.entities.Booking;
import com.cinema.booking.api.v1.reservation.model.Reservation;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    public Booking toEntity(Reservation reservation, String paymentReference) {
        Booking booking = new Booking();
        booking.setUserId(reservation.userId());
        booking.setShowId(reservation.showId());
        booking.setTotalAmount(reservation.totalAmount());
        booking.setDiscountAmount(reservation.discountAmount());
        booking.setFinalAmount(reservation.finalAmount());
        booking.setPaymentReference(paymentReference);
        reservation.seatIds().forEach(booking::addSeat);
        return booking;
    }

    public BookingResponse toResponse(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getUserId(),
                booking.getShowId(),
                booking.getBookedSeats().stream().map(BookedSeat::getSeatId).toList(),
                booking.getTotalAmount(),
                booking.getDiscountAmount(),
                booking.getFinalAmount(),
                booking.getPaymentReference()
        );
    }
}
