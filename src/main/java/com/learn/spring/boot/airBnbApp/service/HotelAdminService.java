package com.learn.spring.boot.airBnbApp.service;

import com.learn.spring.boot.airBnbApp.dto.HotelDto;

public interface HotelAdminService {

    HotelDto createHotel(HotelDto hotelDto);
    HotelDto getHotelById(Long id);

    HotelDto updateHotelById(Long hotelId, HotelDto hotelDto);

    Boolean deleteHotelById(Long hotelId);
    void activeHotel(Long hotelId);

    Object getHotelInfoById(Long hotelId);
}
