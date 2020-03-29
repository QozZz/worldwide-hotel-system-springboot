package com.qozz.worldwidehotelsystem.exception;

public class JwtAuthorizationException extends RuntimeException {

    private static final int HTTP_CODE = 498;
    private static final String HTTP_ERROR = "Token expired or invalid: ";

    public JwtAuthorizationException(RuntimeException cause) {
        super(cause.getMessage(), cause);
    }

    public int getHttpCode() {
        return HTTP_CODE;
    }

    @Override
    public String getMessage() {
        return HTTP_ERROR + this.getCause().getMessage();
    }
}
