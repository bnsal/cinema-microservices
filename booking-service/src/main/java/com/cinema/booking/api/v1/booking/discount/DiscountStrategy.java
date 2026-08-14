package com.cinema.booking.api.v1.booking.discount;

public interface DiscountStrategy {

    int discountPercentageFor(TicketContext context);
}
