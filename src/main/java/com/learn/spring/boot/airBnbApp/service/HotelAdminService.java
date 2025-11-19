package com.learn.spring.boot.airBnbApp.service;

import com.learn.spring.boot.airBnbApp.dto.HotelDto;
import com.learn.spring.boot.airBnbApp.dto.HotelInfoDto;
import com.learn.spring.boot.airBnbApp.dto.HotelInfoRequestDto;

import java.util.List;

public interface HotelAdminService {

    HotelDto createHotel(HotelDto hotelDto);
    HotelDto getHotelById(Long id);

    HotelDto updateHotelById(Long hotelId, HotelDto hotelDto);

    Boolean deleteHotelById(Long hotelId);
    void activeHotel(Long hotelId);

    HotelInfoDto getHotelInfoById(Long hotelId, HotelInfoRequestDto hotelInfoRequestDto);

    List<HotelDto> getAllHotels();
}
