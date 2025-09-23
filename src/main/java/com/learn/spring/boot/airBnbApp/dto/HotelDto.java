package com.learn.spring.boot.airBnbApp.dto;

import com.learn.spring.boot.airBnbApp.entity.ContactInfo;
import com.learn.spring.boot.airBnbApp.entity.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class HotelDto {
    private Long id;

    @NotBlank(message = "Name of Hotel cannot blank")
    @NotNull(message = "Name of Hotel is required field")
    @NotEmpty(message = "Name of Hotel is cannot empty")
    @Size(min = 3, max = 100, message = "Number of characters in name should be in the range: [3, 100]")
    private String name;

    private String city;

    private List<String> photos;

    private List<String> amenities;

    private ContactInfo contactInfo;

    private Boolean active;

    //private User owner;
}
