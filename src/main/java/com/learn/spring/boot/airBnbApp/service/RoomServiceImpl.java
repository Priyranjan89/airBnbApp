package com.learn.spring.boot.airBnbApp.service;

import com.learn.spring.boot.airBnbApp.dto.RoomDto;
import com.learn.spring.boot.airBnbApp.entity.Hotel;
import com.learn.spring.boot.airBnbApp.entity.Room;
import com.learn.spring.boot.airBnbApp.entity.User;
import com.learn.spring.boot.airBnbApp.entity.enums.Role;
import com.learn.spring.boot.airBnbApp.exception.ResourceNotFoundException;
import com.learn.spring.boot.airBnbApp.exception.UnAuthorisedException;
import com.learn.spring.boot.airBnbApp.repository.HotelRepository;
import com.learn.spring.boot.airBnbApp.repository.RoomRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.learn.spring.boot.airBnbApp.util.AppUtils.getCurrentUser;

@Service
@AllArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService{

    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;
    private final HotelRepository hotelRepository;
    private final InventoryService inventoryService;

    @Override
    public RoomDto createNewRoom(Long hotelId, RoomDto roomDto) {
        log.info("Creating a new room in hotel with ID: {}", hotelId);
        Room room = modelMapper.map(roomDto, Room.class);

        isHotelExist(hotelId);
        Hotel hotel = hotelRepository.findById(hotelId).get();

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!(user.equals(hotel.getOwner()) || user.getRoles().contains(Role.HOTEL_MANAGER))){
            throw new UnAuthorisedException("This user does not own this hotel with id: "+hotelId);
        }

        room.setHotel(hotel);
        room = roomRepository.save(room);

        if (hotel.getActive()){
            inventoryService.initializeRoomForAYear(room);
        }
        log.info("Created a new room in hotel with ID: {}", hotelId);
        return modelMapper.map(room, RoomDto.class);
    }

    @Override
    public List<RoomDto> getAllRoomsInHotel(Long hotelId) {
        log.info("Getting all rooms in hotel with ID: {}", hotelId);
        isHotelExist(hotelId);
        Hotel hotel = hotelRepository.findById(hotelId).get();

        return hotel.getRooms()
                .stream()
                .map(room -> modelMapper.map(room, RoomDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public RoomDto getRoomsById(Long roomId) {
        log.info("Getting the room with ID: {}", roomId);
        isRoomExist(roomId);
        Room room = roomRepository.findById(roomId).get();
        return modelMapper.map(room, RoomDto.class);
    }

    @Override
    @Transactional
    public void deleteRoomById(Long roomId) {
        log.info("Deleting the room with ID: {}", roomId);
        isRoomExist(roomId);
        Room room = roomRepository.findById(roomId).get();

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!(user.equals(room.getHotel().getOwner()) || user.getRoles().contains(Role.HOTEL_MANAGER))){
            throw new UnAuthorisedException("This user does not own this room in the hotel with id: "+roomId);
        }

        inventoryService.deleteAllInventoriesForRoom(room);
        roomRepository.deleteById(roomId);
    }

    @Override
    @Transactional
    public RoomDto updateRoomById(Long hotelId, Long roomId, RoomDto roomDto) {
        log.info("Updating the room with ID: {}", roomId);
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: "+hotelId));

        User user = getCurrentUser();
        if(!user.equals(hotel.getOwner())) {
            throw new UnAuthorisedException("This user does not own this hotel with id: "+hotelId);
        }

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: "+roomId));

        modelMapper.map(roomDto, room);
        room.setId(roomId);

//        TODO: if price or inventory is updated, then update the inventory for this room
        room = roomRepository.save(room);

        return modelMapper.map(room, RoomDto.class);
    }

    private void isHotelExist(Long hotelId) {
        boolean exists = hotelRepository.existsById(hotelId);
        if (!exists) throw new ResourceNotFoundException("Hotel not found with id: "+ hotelId);
    }

    private void isRoomExist(Long roomId) {
        boolean exists = roomRepository.existsById(roomId);
        if (!exists) throw new ResourceNotFoundException("Room not found with id: "+ roomId);
    }
}
