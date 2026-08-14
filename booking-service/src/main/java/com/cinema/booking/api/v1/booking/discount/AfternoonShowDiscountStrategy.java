package com.cinema.booking.api.v1.booking.discount;

import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class AfternoonShowDiscountStrategy implements DiscountStrategy {

    private static final int DISCOUNT_PERCENTAGE = 20;
    private static final LocalTime AFTERNOON_START = LocalTime.of(12, 0);
    private static final LocalTime AFTERNOON_END = LocalTime.of(16, 0);

    @Override
    public int discountPercentageFor(TicketContext context) {
        LocalTime startTime = context.showStartTime();
        boolean isAfternoonShow = startTime.isAfter(AFTERNOON_START) && startTime.isBefore(AFTERNOON_END);
        return isAfternoonShow ? DISCOUNT_PERCENTAGE : 0;
    }
}
