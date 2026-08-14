package com.cinema.event.api.v1.show.service;

import com.cinema.event.api.v1.seat.dto.SeatResponse;
import com.cinema.event.api.v1.show.dto.MovieShowRequest;
import com.cinema.event.api.v1.show.dto.MovieShowResponse;

import java.time.LocalDate;
import java.util.List;

public interface MovieShowService {

    MovieShowResponse create(MovieShowRequest request);

    List<MovieShowResponse> getAll(Long eventId, LocalDate showDate);

    MovieShowResponse getById(Long showId);

    List<SeatResponse> getSeats(Long showId);
}
