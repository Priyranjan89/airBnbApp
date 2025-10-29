package com.learn.spring.boot.airBnbApp.service;

import com.learn.spring.boot.airBnbApp.dto.HotelDto;
import com.learn.spring.boot.airBnbApp.dto.HotelInfoDto;
import com.learn.spring.boot.airBnbApp.dto.RoomDto;
import com.learn.spring.boot.airBnbApp.entity.Hotel;
import com.learn.spring.boot.airBnbApp.entity.Room;
import com.learn.spring.boot.airBnbApp.entity.User;
import com.learn.spring.boot.airBnbApp.exception.ResourceNotFoundException;
import com.learn.spring.boot.airBnbApp.exception.UnAuthorisedException;
import com.learn.spring.boot.airBnbApp.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelAdminAdminServiceImpl implements HotelAdminService {

    private final HotelRepository hotelRepository;

    private final ModelMapper modelMapper;

    private final InventoryService inventoryService;

    private final RoomService roomService;

    @Override
    public HotelDto createHotel(HotelDto hotelDto) {
        log.info("Creating a new hotel with name: {}", hotelDto.getName());
        Hotel hotel = modelMapper.map(hotelDto, Hotel.class);
        hotel.setActive(false);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        hotel.setOwner(user);
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

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())) {
            throw new UnAuthorisedException("This user does not own this hotel with id: "+hotelId);
        }

        log.info("Fetched the hotel with id: {}", hotelId);
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto updateHotelById(Long hotelId, HotelDto hotelDto) {
        log.info("Updating the hotel with id: {}", hotelId);
        isHotelExist(hotelId);

        Hotel exitingHotel = hotelRepository.findById(hotelId).get();

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(exitingHotel.getOwner())) {
            throw new UnAuthorisedException("This user does not own this hotel with id: "+hotelId);
        }

        Hotel hotel = modelMapper.map(hotelDto, Hotel.class);
        hotel.setId(exitingHotel.getId());
        Hotel saveHotel = hotelRepository.save(hotel);
        log.info("Updated the hotel with id: {}", hotelId);

        return modelMapper.map(saveHotel, HotelDto.class);
    }

    @Override
    @Transactional
    public Boolean deleteHotelById(Long hotelId) {
        isHotelExist(hotelId);
        Hotel hotel = hotelRepository.findById(hotelId).get();

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())) {
            throw new UnAuthorisedException("This user does not own this hotel with id: "+hotelId);
        }

        for(Room room: hotel.getRooms()) {
            inventoryService.deleteFutureInventories(room);
            roomService.deleteRoomById(room.getId());
        }
        hotelRepository.deleteById(hotelId);
        return true;
    }

    @Override
    @Transactional
    public void activeHotel(Long hotelId) {
        log.info("Activating the hotel with id: {}", hotelId);
        isHotelExist(hotelId);
        Hotel hotel = hotelRepository.findById(hotelId).get();

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())) {
            throw new UnAuthorisedException("This user does not own this hotel with id: "+hotelId);
        }

        hotel.setActive(true);

        //assuming only do it once
        for (Room room : hotel.getRooms()){
            inventoryService.initializeRoomForAYear(room);
        }
        //hotelRepository.save(hotel);
        log.info("Hotel Got Activated with id: {}", hotelId);
    }
    /*Public Method*/
    @Override
    public HotelInfoDto getHotelInfoById(Long hotelId) {
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: "+hotelId));

        List<RoomDto> rooms = hotel.getRooms()
                .stream()
                .map((element) -> modelMapper.map(element, RoomDto.class))
                .toList();

        return new HotelInfoDto(modelMapper.map(hotel, HotelDto.class), rooms);
    }

    private void isHotelExist(Long hotelId) {
        boolean exists = hotelRepository.existsById(hotelId);
        if (!exists) throw new ResourceNotFoundException("Hotel not found with id: "+ hotelId);
    }
}
