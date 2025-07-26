package com.learn.spring.boot.airBnbApp.dto;

import com.learn.spring.boot.airBnbApp.entity.ContactInfo;
import lombok.Data;

import java.util.List;

@Data
public class HotelDto {
    private Long id;

    private String name;

    private String city;

    private List<String> photos;

    private List<String> amenities;

    private ContactInfo contactInfo;

    private Boolean active;
}
