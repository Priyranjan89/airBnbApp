package com.learn.spring.boot.airBnbApp.controller;

import com.learn.spring.boot.airBnbApp.dto.BookingDto;
import com.learn.spring.boot.airBnbApp.dto.HotelDto;
import com.learn.spring.boot.airBnbApp.dto.HotelReportDto;
import com.learn.spring.boot.airBnbApp.service.BookingService;
import com.learn.spring.boot.airBnbApp.service.HotelAdminService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/admin/hotels")
@RequiredArgsConstructor
@Slf4j
public class HotelAdminController {

    private final HotelAdminService hotelAdminService;
    private final BookingService bookingService;

    @GetMapping("/{hotelId}")
    @Operation(summary = "Create a new hotel", tags = {"Admin Hotel"})
    public ResponseEntity<HotelDto> getHotelById(@PathVariable Long hotelId){
        log.info("Getting the hotel with id: {}", hotelId);
        HotelDto hotel = hotelAdminService.getHotelById(hotelId);
        log.info("Got the hotel with id: {}", hotelId);
        return ResponseEntity.ok(hotel);
    }

    @PostMapping
    @Operation(summary = "Get a hotel by Id", tags = {"Admin Hotel"})
    public ResponseEntity<HotelDto> createNewHotel(@RequestBody HotelDto hotelDto){
        log.info("Attempting to create a new hotel with name: "+hotelDto.getName());
        HotelDto createdHotelDto = hotelAdminService.createHotel(hotelDto);
        log.info("New hotel created with name: "+hotelDto.getName());
        return new ResponseEntity<>(createdHotelDto, HttpStatus.CREATED);
    }

    @PutMapping("/{hotelId}")
    @Operation(summary = "Update a hotel", tags = {"Admin Hotel"})
    public ResponseEntity<HotelDto> updateHotelById(@PathVariable Long hotelId, @RequestBody HotelDto hotelDto){
        HotelDto hotel = hotelAdminService.updateHotelById(hotelId, hotelDto);
        return ResponseEntity.ok(hotel);
    }

    @DeleteMapping("/{hotelId}")
    @Operation(summary = "Delete a hotel", tags = {"Admin Hotel"})
    public ResponseEntity<Void> deleteHotelById(@PathVariable Long hotelId){
       hotelAdminService.deleteHotelById(hotelId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{hotelId}/activate")
    @Operation(summary = "Activate a hotel", tags = {"Admin Hotel"})
    public ResponseEntity<Void> activeHotel(@PathVariable Long hotelId){
        hotelAdminService.activeHotel(hotelId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Get all hotels owned by admin", tags = {"Admin Hotel"})
    public ResponseEntity<List<HotelDto>> getAllHotels() {
        return ResponseEntity.ok(hotelAdminService.getAllHotels());
    }

    @GetMapping("/{hotelId}/bookings")
    @Operation(summary = "Get all bookings of a hotel", tags = {"Admin Bookings"})
    public ResponseEntity<List<BookingDto>> getAllBookingsByHotelId(@PathVariable Long hotelId) {
        return ResponseEntity.ok(bookingService.getAllBookingsByHotelId(hotelId));
    }

    @GetMapping("/{hotelId}/reports")
    @Operation(summary = "Generate a bookings report of a hotel", tags = {"Admin Bookings"})
    public ResponseEntity<HotelReportDto> getHotelReport(@PathVariable Long hotelId,
                                                         @RequestParam(required = false) LocalDate startDate,
                                                         @RequestParam(required = false) LocalDate endDate) {

        if (startDate == null) startDate = LocalDate.now().minusMonths(1);
        if (endDate == null) endDate = LocalDate.now();

        return ResponseEntity.ok(bookingService.getHotelReport(hotelId, startDate, endDate));
    }

}
