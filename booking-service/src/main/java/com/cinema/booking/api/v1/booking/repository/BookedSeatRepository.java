package com.cinema.booking.api.v1.booking.repository;

import com.cinema.booking.api.v1.booking.entities.BookedSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface BookedSeatRepository extends JpaRepository<BookedSeat, Long> {

    List<BookedSeat> findByShowId(Long showId);

    List<BookedSeat> findByShowIdAndSeatIdIn(Long showId, Collection<Long> seatIds);
}
