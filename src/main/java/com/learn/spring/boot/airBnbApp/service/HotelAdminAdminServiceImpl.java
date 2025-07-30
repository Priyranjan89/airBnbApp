package com.learn.spring.boot.airBnbApp.service;

import com.learn.spring.boot.airBnbApp.dto.HotelDto;
import com.learn.spring.boot.airBnbApp.entity.Hotel;
import com.learn.spring.boot.airBnbApp.entity.Room;
import com.learn.spring.boot.airBnbApp.exception.ResourceNotFoundException;
import com.learn.spring.boot.airBnbApp.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelAdminAdminServiceImpl implements HotelAdminService {

    private final HotelRepository hotelRepository;

    private final ModelMapper modelMapper;

    private final InventoryService inventoryService;

    @Override
    public HotelDto createHotel(HotelDto hotelDto) {
        log.info("Creating a new hotel with name: {}", hotelDto.getName());
        Hotel hotel = modelMapper.map(hotelDto, Hotel.class);
        hotel.setActive(false);
        Hotel saveHotel = hotelRepository.save(hotel);
        log.info("Create a new hotel with name: {}", hotelDto.getName());
        return modelMapper.map(saveHotel, HotelDto.class);
    }

    @Override
    public HotelDto getHotelById(Long hotelId) {
        log.info("Fetching the hotel with id: {}", hotelId);
        Hotel hotel = hotelRepository.
                findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: "+hotelId));
        log.info("Fetched the hotel with id: {}", hotelId);
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto updateHotelById(Long hotelId, HotelDto hotelDto) {
        log.info("Updating the hotel with id: {}", hotelId);
        isHotelExist(hotelId);

        Hotel exitingHotel = hotelRepository.findById(hotelId).get();

        Hotel hotel = modelMapper.map(hotelDto, Hotel.class);
        hotel.setId(exitingHotel.getId());
        Hotel saveHotel = hotelRepository.save(hotel);
        log.info("Updated the hotel with id: {}", hotelId);

        return modelMapper.map(saveHotel, HotelDto.class);
    }

    @Override
    public Boolean deleteHotelById(Long hotelId) {
        isHotelExist(hotelId);
        Hotel hotel = hotelRepository.findById(hotelId).get();
        hotelRepository.deleteById(hotelId);
        for(Room room: hotel.getRooms()) {
            inventoryService.deleteFutureInventories(room);
        }
        return true;
    }

    @Override
    public void activeHotel(Long hotelId) {
        log.info("Activating the hotel with id: {}", hotelId);
        isHotelExist(hotelId);
        Hotel hotel = hotelRepository.findById(hotelId).get();
        hotel.setActive(true);

        //assuming only do it once
        for (Room room : hotel.getRooms()){
            inventoryService.initializeRoomForAYear(room);
        }

        hotelRepository.save(hotel);
        log.info("Hotel Got Activated with id: {}", hotelId);
    }

    private void isHotelExist(Long hotelId) {
        boolean exists = hotelRepository.existsById(hotelId);
        if (!exists) throw new ResourceNotFoundException("Hotel not found with id: "+ hotelId);
    }
}
