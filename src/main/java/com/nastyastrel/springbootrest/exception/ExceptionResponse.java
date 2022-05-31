package com.nastyastrel.springbootrest.exception;


import java.time.LocalDateTime;

public record ExceptionResponse(LocalDateTime localDateTime, String message) {

}
