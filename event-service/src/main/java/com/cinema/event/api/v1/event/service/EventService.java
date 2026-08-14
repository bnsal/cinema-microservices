package com.cinema.event.api.v1.event.service;

import com.cinema.event.api.v1.event.dto.EventRequest;
import com.cinema.event.api.v1.event.dto.EventResponse;
import com.cinema.event.api.v1.event.entities.EventType;

import java.util.List;

public interface EventService {

    EventResponse create(EventRequest request);

    List<EventResponse> getAll(EventType type);

    EventResponse getById(Long eventId);
}
