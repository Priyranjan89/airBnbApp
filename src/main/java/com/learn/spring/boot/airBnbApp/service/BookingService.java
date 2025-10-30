package com.learn.spring.boot.airBnbApp.service;

import com.learn.spring.boot.airBnbApp.dto.BookingDto;
import com.learn.spring.boot.airBnbApp.dto.BookingRequest;
import com.learn.spring.boot.airBnbApp.dto.GuestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookingService {

    BookingDto initialiseBooking(BookingRequest bookingRequest);

    BookingDto addGuests(Long bookingId, List<GuestDto> guestDtoList);

    Page<BookingDto> getAllBookings(Pageable pageable);

    Page<BookingDto> getBookingsByUserId(Long userId, Pageable pageable);

    Page<BookingDto> getMyBookings(Pageable pageable);

    BookingDto getBookingById(Long bookingId);
}
