package com.learn.spring.boot.airBnbApp.advice;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public abstract class ApiResponse<T> {
    @JsonFormat(pattern = "hh:mm:ss dd-MM-yyyy")
    private final LocalDateTime timeStamp = LocalDateTime.now();
    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }
}