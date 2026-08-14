package com.cinema.event.api.v1.theater.service;

import com.cinema.event.api.v1.theater.dto.TheaterRequest;
import com.cinema.event.api.v1.theater.dto.TheaterResponse;

import java.util.List;

public interface TheaterService {

    TheaterResponse create(TheaterRequest request);

    List<TheaterResponse> getAll(String city);

    TheaterResponse getById(Long theaterId);
}
