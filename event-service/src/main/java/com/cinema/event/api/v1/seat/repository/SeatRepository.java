package com.cinema.event.api.v1.seat.repository;

import com.cinema.event.api.v1.seat.entities.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByScreenIdOrderByRowLabelAscSeatNumberAsc(Long screenId);

    boolean existsByScreenId(Long screenId);
}
