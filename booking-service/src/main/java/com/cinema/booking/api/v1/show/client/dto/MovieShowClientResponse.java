package com.cinema.booking.api.v1.show.client.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record MovieShowClientResponse(
        Long id,
        Long eventId,
        String eventTitle,
        Long screenId,
        LocalDate showDate,
        LocalTime startTime,
        Integer basePrice
) {
}
