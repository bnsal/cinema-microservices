package com.cinema.booking.api.v1.booking.discount;

import java.time.LocalTime;

public record TicketContext(int position, LocalTime showStartTime) {
}
