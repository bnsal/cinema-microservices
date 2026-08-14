package com.cinema.event.api.v1.seat.service;

import com.cinema.event.api.v1.seat.dto.SeatResponse;

import java.util.List;

public interface SeatService {

    List<SeatResponse> generate(Long screenId);

    List<SeatResponse> getAll(Long screenId);
}
