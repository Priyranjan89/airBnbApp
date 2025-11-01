package com.learn.spring.boot.airBnbApp.service;

import com.learn.spring.boot.airBnbApp.dto.HotelDto;
import com.learn.spring.boot.airBnbApp.dto.HotelSearchRequest;
import com.learn.spring.boot.airBnbApp.dto.InventoryDto;
import com.learn.spring.boot.airBnbApp.dto.UpdateInventoryRequestDto;
import com.learn.spring.boot.airBnbApp.entity.Room;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InventoryService {
    void initializeRoomForAYear(Room room);

    void deleteFutureInventories(Room room);

    void deleteAllInventoriesForRoom(Room room);

    Page<HotelDto> searchHotels(HotelSearchRequest hotelSearchRequest);

    List<InventoryDto> getAllInventoryByRoom(Long roomId);

    void updateInventory(Long roomId, UpdateInventoryRequestDto updateInventoryRequestDto);
}
