package com.cinema.event.api.v1.show.repository;

import com.cinema.event.api.v1.show.entities.MovieShow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MovieShowRepository extends JpaRepository<MovieShow, Long> {

    List<MovieShow> findByEventIdAndShowDateOrderByStartTimeAsc(Long eventId, LocalDate showDate);

    List<MovieShow> findByScreenIdAndShowDateBetween(Long screenId, LocalDate fromDate, LocalDate toDate);
}
