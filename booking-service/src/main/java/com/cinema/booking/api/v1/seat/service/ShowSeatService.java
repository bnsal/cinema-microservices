package com.cinema.booking.api.v1.seat.service;

import com.cinema.booking.api.v1.seat.dto.ShowSeatResponse;

import java.util.List;

public interface ShowSeatService {

    List<ShowSeatResponse> getShowSeats(Long showId);
}
