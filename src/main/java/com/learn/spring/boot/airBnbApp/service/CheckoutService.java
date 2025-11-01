package com.learn.spring.boot.airBnbApp.service;


import com.learn.spring.boot.airBnbApp.entity.Booking;

public interface CheckoutService {

    String getCheckoutSession(Booking booking, String successUrl, String failureUrl);

}
