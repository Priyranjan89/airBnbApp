package com.learn.spring.boot.airBnbApp.advice;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@Builder
public class ApiSubError {

    private HttpStatus status;
    private String message;
    private List<String> subErrors;
}
