package com.cinema.event.api.v1.screen.service;

import com.cinema.event.api.v1.screen.dto.ScreenRequest;
import com.cinema.event.api.v1.screen.dto.ScreenResponse;

import java.util.List;

public interface ScreenService {

    ScreenResponse create(Long theaterId, ScreenRequest request);

    List<ScreenResponse> getAll(Long theaterId);

    ScreenResponse getById(Long theaterId, Long screenId);
}
