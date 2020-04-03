package com.qozz.worldwidehotelsystem.exception;

public class RoomDoesNotExistException extends RuntimeException {
    public RoomDoesNotExistException(String message) {
        super(message);
    }
}
