package com.cinema.event.api.v1.event.service.impl;

import com.cinema.event.api.v1.event.dto.EventRequest;
import com.cinema.event.api.v1.event.dto.EventResponse;
import com.cinema.event.api.v1.event.entities.Event;
import com.cinema.event.api.v1.event.entities.EventType;
import com.cinema.event.api.v1.event.exceptions.EventNotFoundException;
import com.cinema.event.api.v1.event.mapper.EventMapper;
import com.cinema.event.api.v1.event.repository.EventRepository;
import com.cinema.event.api.v1.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Override
    @Transactional
    public EventResponse create(EventRequest request) {
        Event event = eventMapper.toEntity(request);
        event = eventRepository.save(event);
        return eventMapper.toResponse(event);
    }

    @Override
    public List<EventResponse> getAll(EventType type) {
        List<Event> events = type == null
                ? eventRepository.findAll()
                : eventRepository.findByTypeOrderByTitleAsc(type);
        return events.stream().map(eventMapper::toResponse).toList();
    }

    @Override
    public EventResponse getById(Long eventId) {
        return eventMapper.toResponse(findById(eventId));
    }

    private Event findById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
    }
}
