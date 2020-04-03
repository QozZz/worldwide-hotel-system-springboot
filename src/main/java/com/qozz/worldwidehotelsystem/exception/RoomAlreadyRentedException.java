package com.qozz.worldwidehotelsystem.exception;

public class RoomAlreadyRentedException extends RuntimeException{
    public RoomAlreadyRentedException(String message) {
        super(message);
    }
}
