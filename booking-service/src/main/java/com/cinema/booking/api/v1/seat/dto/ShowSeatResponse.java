package com.cinema.booking.api.v1.seat.dto;

public record ShowSeatResponse(
        Long seatId,
        String seatCode,
        SeatStatus status
) {
}
