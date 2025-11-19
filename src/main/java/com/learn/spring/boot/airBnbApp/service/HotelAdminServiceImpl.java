package com.learn.spring.boot.airBnbApp.service;

import com.learn.spring.boot.airBnbApp.dto.HotelDto;
import com.learn.spring.boot.airBnbApp.dto.HotelInfoDto;
import com.learn.spring.boot.airBnbApp.dto.HotelInfoRequestDto;
import com.learn.spring.boot.airBnbApp.dto.RoomPriceDto;
import com.learn.spring.boot.airBnbApp.dto.RoomPriceResponseDto;
import com.learn.spring.boot.airBnbApp.entity.Hotel;
import com.learn.spring.boot.airBnbApp.entity.Room;
import com.learn.spring.boot.airBnbApp.entity.User;
import com.learn.spring.boot.airBnbApp.entity.enums.Role;
import com.learn.spring.boot.airBnbApp.exception.ResourceNotFoundException;
import com.learn.spring.boot.airBnbApp.exception.UnAuthorisedException;
import com.learn.spring.boot.airBnbApp.repository.HotelRepository;
import com.learn.spring.boot.airBnbApp.repository.InventoryRepository;
import com.learn.spring.boot.airBnbApp.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static com.learn.spring.boot.airBnbApp.util.AppUtils.getCurrentUser;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelAdminServiceImpl implements HotelAdminService {

    private final HotelRepository hotelRepository;

    private final ModelMapper modelMapper;

    private final InventoryService inventoryService;

    private final RoomService roomService;

    private final RoomRepository roomRepository;

    private final InventoryRepository inventoryRepository;

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
        if(!(user.equals(hotel.getOwner()) || user.getRoles().contains(Role.HOTEL_MANAGER))) {
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
        if(!(user.equals(exitingHotel.getOwner()) || user.getRoles().contains(Role.HOTEL_MANAGER))) {
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
        if(!(user.equals(hotel.getOwner()) || user.getRoles().contains(Role.HOTEL_MANAGER))) {
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

        if(!(user.equals(hotel.getOwner()) || user.getRoles().contains(Role.HOTEL_MANAGER))) {
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

    @Override
    public HotelInfoDto getHotelInfoById(Long hotelId, HotelInfoRequestDto hotelInfoRequestDto) {
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: "+hotelId));

        long daysCount = ChronoUnit.DAYS.between(hotelInfoRequestDto.getStartDate(), hotelInfoRequestDto.getEndDate())+1;

        List<RoomPriceDto> roomPriceDtoList = inventoryRepository.findRoomAveragePrice(hotelId,
                hotelInfoRequestDto.getStartDate(), hotelInfoRequestDto.getEndDate(),
                hotelInfoRequestDto.getRoomsCount(), daysCount);

        List<RoomPriceResponseDto> rooms = roomPriceDtoList.stream()
                .map(roomPriceDto -> {
                    RoomPriceResponseDto roomPriceResponseDto = modelMapper.map(roomPriceDto.getRoom(),
                            RoomPriceResponseDto.class);
                    roomPriceResponseDto.setPrice(roomPriceDto.getAveragePrice());
                    return roomPriceResponseDto;
                })
                .collect(Collectors.toList());

        return new HotelInfoDto(modelMapper.map(hotel, HotelDto.class), rooms);
    }
    private void isHotelExist(Long hotelId) {
        boolean exists = hotelRepository.existsById(hotelId);
        if (!exists) throw new ResourceNotFoundException("Hotel not found with id: "+ hotelId);
    }

    @Override
    public List<HotelDto> getAllHotels() {
        User user = getCurrentUser();
        log.info("Getting all hotels for the admin user with ID: {}", user.getId());
        List<Hotel> hotels = hotelRepository.findByOwner(user);

        return hotels
                .stream()
                .map((element) -> modelMapper.map(element, HotelDto.class))
                .collect(Collectors.toList());
    }
}
