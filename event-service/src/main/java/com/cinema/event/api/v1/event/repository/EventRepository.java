package com.cinema.event.api.v1.event.repository;

import com.cinema.event.api.v1.event.entities.Event;
import com.cinema.event.api.v1.event.entities.EventType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByTypeOrderByTitleAsc(EventType type);
}
