package com.cinema.event.api.v1.screen.repository;

import com.cinema.event.api.v1.screen.entities.Screen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScreenRepository extends JpaRepository<Screen, Long> {

    List<Screen> findByTheaterIdOrderByNameAsc(Long theaterId);

    Optional<Screen> findByTheaterIdAndId(Long theaterId, Long screenId);

    boolean existsByTheaterIdAndNameIgnoreCase(Long theaterId, String name);
}
