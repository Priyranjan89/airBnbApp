package com.learn.spring.boot.airBnbApp.service;

import com.learn.spring.boot.airBnbApp.dto.HotelDto;
import com.learn.spring.boot.airBnbApp.entity.Hotel;

public interface HotelService {

    public Hotel createHotel(HotelDto hotelDto);
    public Hotel getHotelById(Long id);
}
