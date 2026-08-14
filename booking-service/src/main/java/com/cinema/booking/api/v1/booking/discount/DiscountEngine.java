package com.cinema.booking.api.v1.booking.discount;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class DiscountEngine {

    private final List<DiscountStrategy> discountStrategies;

    public int bestDiscountPercentageFor(TicketContext context) {
        return discountStrategies.stream()
                .mapToInt(strategy -> strategy.discountPercentageFor(context))
                .max()
                .orElse(0);
    }
}
