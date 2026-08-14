package com.cinema.booking.api.v1.booking.discount;

import org.springframework.stereotype.Component;

@Component
public class ThirdTicketDiscountStrategy implements DiscountStrategy {

    private static final int DISCOUNT_PERCENTAGE = 50;
    private static final int NTH_TICKET = 3;

    @Override
    public int discountPercentageFor(TicketContext context) {
        return context.position() == NTH_TICKET ? DISCOUNT_PERCENTAGE : 0;
    }
}
