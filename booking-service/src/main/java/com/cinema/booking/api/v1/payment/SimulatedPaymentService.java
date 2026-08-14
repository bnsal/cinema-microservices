package com.cinema.booking.api.v1.payment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class SimulatedPaymentService implements PaymentService {

    @Override
    public String pay(Long userId, Integer amount) {
        //TO DO
        return "ok";
    }
}
