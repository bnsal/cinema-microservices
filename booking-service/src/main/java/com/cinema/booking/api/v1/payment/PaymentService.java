package com.cinema.booking.api.v1.payment;


public interface PaymentService {

    String pay(Long userId, Integer amount);
}
