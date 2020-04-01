package com.qozz.worldwidehotelsystem.exception;

public class RoomAlreadyRented extends RuntimeException{
    public RoomAlreadyRented(String message) {
        super(message);
    }
}
