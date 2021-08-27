package com.jema.cinema.exception;

public class MainRequestException extends RuntimeException{
    public MainRequestException(String message){
        super(message);
    }

    public MainRequestException(String message, Throwable cause){
        super(message, cause);
    }
}
