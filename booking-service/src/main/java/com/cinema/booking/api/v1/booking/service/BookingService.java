package com.cinema.booking.api.v1.booking.service;

import com.cinema.booking.api.v1.booking.dto.BookingRequest;
import com.cinema.booking.api.v1.booking.dto.BookingResponse;

import java.util.List;

public interface BookingService {

    BookingResponse create(BookingRequest request);

    BookingResponse getById(Long bookingId);

    List<BookingResponse> getAllByUser(Long userId);
}
