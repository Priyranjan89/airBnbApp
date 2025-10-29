package com.learn.spring.boot.airBnbApp.strategy;


import com.learn.spring.boot.airBnbApp.entity.Inventory;

import java.math.BigDecimal;
public interface PricingStrategy {

    BigDecimal calculatePrice(Inventory inventory);
}
