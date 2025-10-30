package com.learn.spring.boot.airBnbApp.dto;

import com.learn.spring.boot.airBnbApp.entity.enums.Role;
import lombok.Data;

import java.util.Set;

@Data
public class UserDto {

    private Long id;
    private String name;
    private String email;
    private String password;
    private Role role;
}
