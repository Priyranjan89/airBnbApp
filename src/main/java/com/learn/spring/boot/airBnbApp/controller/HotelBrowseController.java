package com.learn.spring.boot.airBnbApp.controller;


import com.learn.spring.boot.airBnbApp.dto.HotelDto;
import com.learn.spring.boot.airBnbApp.dto.HotelInfoDto;
import com.learn.spring.boot.airBnbApp.dto.HotelInfoRequestDto;
import com.learn.spring.boot.airBnbApp.dto.HotelSearchRequest;
import com.learn.spring.boot.airBnbApp.service.HotelAdminService;
import com.learn.spring.boot.airBnbApp.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelBrowseController {

    private final InventoryService inventoryService;
    private final HotelAdminService hotelService;

    @GetMapping("/search")
    @Operation(summary = "Search hotels", tags = {"Browse Hotels"})
    public ResponseEntity<Page<HotelDto>> searchHotels(@RequestBody HotelSearchRequest hotelSearchRequest) {

        Page<HotelDto> page = inventoryService.searchHotels(hotelSearchRequest);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{hotelId}/info")
    @Operation(summary = "Get a hotel info by hotelId", tags = {"Browse Hotels"})
    public ResponseEntity<HotelInfoDto> getHotelInfo(@PathVariable Long hotelId, @RequestBody HotelInfoRequestDto hotelInfoRequestDto) {
        return ResponseEntity.ok(hotelService.getHotelInfoById(hotelId, hotelInfoRequestDto));
    }

}
