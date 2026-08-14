package com.cinema.event.api.v1.theater.repository;

import com.cinema.event.api.v1.theater.entities.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TheaterRepository extends JpaRepository<Theater, Long> {

    List<Theater> findByCityIgnoreCaseOrderByNameAsc(String city);
}
