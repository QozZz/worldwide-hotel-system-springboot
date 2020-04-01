package com.qozz.worldwidehotelsystem.exception;

public class RoomDoesNotExist extends RuntimeException {
    public RoomDoesNotExist(String message) {
        super(message);
    }
}
