package com.learn.spring.boot.airBnbApp.service;


import com.learn.spring.boot.airBnbApp.dto.ProfileUpdateRequestDto;
import com.learn.spring.boot.airBnbApp.dto.UserDto;
import com.learn.spring.boot.airBnbApp.entity.User;

public interface UserService {

    User getUserById(Long id);

    void updateProfile(ProfileUpdateRequestDto profileUpdateRequestDto);

    UserDto getMyProfile();

}
