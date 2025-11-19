package com.learn.spring.boot.airBnbApp.dto;

import com.learn.spring.boot.airBnbApp.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomPriceDto {
    private Room room;
    private Double averagePrice;
}
