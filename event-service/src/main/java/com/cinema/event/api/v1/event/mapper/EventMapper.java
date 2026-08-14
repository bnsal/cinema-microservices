package com.cinema.event.api.v1.event.mapper;

import com.cinema.event.api.v1.event.dto.EventRequest;
import com.cinema.event.api.v1.event.dto.EventResponse;
import com.cinema.event.api.v1.event.entities.Event;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    public Event toEntity(EventRequest request) {
        Event event = new Event();
        event.setTitle(request.title());
        event.setType(request.type());
        event.setLanguage(request.language());
        event.setDurationMinutes(request.durationMinutes());
        return event;
    }

    public EventResponse toResponse(Event event) {
        return new EventResponse(
                event.getId(),
                event.getTitle(),
                event.getType(),
                event.getLanguage(),
                event.getDurationMinutes()
        );
    }
}
