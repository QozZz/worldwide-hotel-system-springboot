package com.qozz.worldwidehotelsystem.exception;

public class HotelDoesNotExistException extends RuntimeException{
    public HotelDoesNotExistException(String message) {
        super(message);
    }
}
