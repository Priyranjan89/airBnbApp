package com.learn.spring.boot.airBnbApp.service;

import com.learn.spring.boot.airBnbApp.entity.Room;

public interface InventoryService {
    void initializeRoomForAYear(Room room);

    void deleteFutureInventories(Room room);
}
