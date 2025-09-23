package com.learn.spring.boot.airBnbApp.service;

import com.learn.spring.boot.airBnbApp.dto.HotelDto;
import com.learn.spring.boot.airBnbApp.dto.HotelSearchRequest;
import com.learn.spring.boot.airBnbApp.entity.Room;
import org.springframework.data.domain.Page;

public interface InventoryService {
    void initializeRoomForAYear(Room room);

    void deleteFutureInventories(Room room);

    void deleteAllInventoriesForRoom(Room room);

    Page<HotelDto> searchHotels(HotelSearchRequest hotelSearchRequest);
}
