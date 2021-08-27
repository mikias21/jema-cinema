package com.jema.cinema.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class MainExceptionHandler {
    @ExceptionHandler(value = {MainRequestException.class})
    public ResponseEntity<Object> handleMainException(MainRequestException e){
        MainException mainException =
                new MainException(e.getMessage(), e.getCause(), HttpStatus.BAD_REQUEST, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(mainException, HttpStatus.BAD_REQUEST);
    }
}
