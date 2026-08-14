package com.cinema.booking.api.v1.reservation.mapper;

import com.cinema.booking.api.v1.reservation.dto.ReservationResponse;
import com.cinema.booking.api.v1.reservation.model.Reservation;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    public ReservationResponse toResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.id(),
                reservation.userId(),
                reservation.showId(),
                reservation.seatIds(),
                reservation.totalAmount(),
                reservation.discountAmount(),
                reservation.finalAmount(),
                reservation.expiresAt()
        );
    }
}
