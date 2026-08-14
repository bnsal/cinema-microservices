package com.cinema.event.api.v1.show.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record MovieShowResponse(
        Long id,

        Long eventId,
        String eventTitle,

        Long theaterId,
        String theaterName,

        Long screenId,
        String screenName,

        LocalDate showDate,
        LocalTime startTime,
        Integer durationMinutes,

        Integer basePrice
) {
}
