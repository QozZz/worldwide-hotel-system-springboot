package com.qozz.worldwidehotelsystem.exception;

public class HotelDoesNotExist extends RuntimeException{
    public HotelDoesNotExist(String message) {
        super(message);
    }
}
