package com.learn.spring.boot.airBnbApp.service;

import com.learn.spring.boot.airBnbApp.dto.HotelDto;
import com.learn.spring.boot.airBnbApp.entity.Hotel;
import com.learn.spring.boot.airBnbApp.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService{

    private final HotelRepository hotelRepository;

    @Override
    public Hotel createHotel(HotelDto hotelDto) {
        return null;
    }

    @Override
    public Hotel getHotelById(Long id) {
        return null;
    }
}
