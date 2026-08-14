package com.cinema.booking.api.v1.booking.repository;

import com.cinema.booking.api.v1.booking.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserIdOrderByIdDesc(Long userId);
}
