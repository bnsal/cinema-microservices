package com.cinema.event.api.v1.seat.mapper;

import com.cinema.event.api.v1.seat.dto.SeatResponse;
import com.cinema.event.api.v1.seat.entities.Seat;
import org.springframework.stereotype.Component;

@Component
public class SeatMapper {

    public SeatResponse toResponse(Seat seat) {
        return new SeatResponse(
                seat.getId(),
                seat.getScreen().getId(),
                seat.getRowLabel(),
                seat.getSeatNumber(),
                seat.getRowLabel() + seat.getSeatNumber()
        );
    }
}
