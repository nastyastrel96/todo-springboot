package com.nastyastrel.springbootrest.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@ControllerAdvice
public class TodoExceptionHandler {
    final Logger logger = LoggerFactory.getLogger(NoWordForFilteringFoundException.class);

    @ExceptionHandler(NoWordForFilteringFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNoWordForFilteringException(NoWordForFilteringFoundException exception) {
        logger.error("There is no item to be found with this word", exception);
        ExceptionResponse response = new ExceptionResponse(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoSuchIdItemException.class)
    public ResponseEntity<ExceptionResponse> handleNoSuchIdItemException(NoSuchIdItemException exception) {
        logger.error("There is no item to be found with this id", exception);
        ExceptionResponse response = new ExceptionResponse(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoSuchIdTagException.class)
    public ResponseEntity<ExceptionResponse> handleNoSuchIdTagException(NoSuchIdTagException exception) {
        logger.error("There is no tag to be found with this id", exception);
        ExceptionResponse response = new ExceptionResponse(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoSuchTagException.class)
    public ResponseEntity<ExceptionResponse> handleNoSuchTagException(NoSuchTagException exception) {
        logger.error("There is no tag to be found with this id", exception);
        ExceptionResponse response = new ExceptionResponse(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoSuchItemAndTagException.class)
    public ResponseEntity<ExceptionResponse> handleNoSuchTagException(NoSuchItemAndTagException exception) {
        logger.error("There is no item with this data to filter", exception);
        ExceptionResponse response = new ExceptionResponse(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
