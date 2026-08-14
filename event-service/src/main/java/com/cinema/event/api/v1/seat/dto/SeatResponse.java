package com.cinema.event.api.v1.seat.dto;

public record SeatResponse(
        Long id,
        Long screenId,
        String rowLabel,
        Integer seatNumber,
        String seatCode
) {
}
