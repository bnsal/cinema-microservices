package com.cinema.booking.api.v1.reservation.service;

import com.cinema.booking.api.v1.reservation.dto.ReservationRequest;
import com.cinema.booking.api.v1.reservation.dto.ReservationResponse;

public interface ReservationService {

    ReservationResponse reserve(ReservationRequest request);

    ReservationResponse getById(String reservationId);
}
