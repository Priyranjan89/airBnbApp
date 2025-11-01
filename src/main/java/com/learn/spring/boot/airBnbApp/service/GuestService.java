package com.learn.spring.boot.airBnbApp.service;

import com.learn.spring.boot.airBnbApp.dto.GuestDto;

import java.util.List;

public interface GuestService {

    List<GuestDto> getAllGuests();

    void updateGuest(Long guestId, GuestDto guestDto);

    void deleteGuest(Long guestId);

    GuestDto addNewGuest(GuestDto guestDto);
}
