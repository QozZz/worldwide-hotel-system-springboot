package com.qozz.worldwidehotelsystem.handler;

import com.qozz.worldwidehotelsystem.data.dto.ErrorResponse;
import com.qozz.worldwidehotelsystem.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ResponseEntityExceptionHandler {

    @ExceptionHandler(value = AuthException.class)
    public ResponseEntity<ErrorResponse> authException(AuthException exception) {
        return new ResponseEntity<>(
                createErrorResponse(exception.getMessage()),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = JwtAuthorizationException.class)
    public ResponseEntity<ErrorResponse> jwtAuthorizationException(JwtAuthorizationException exception) {
        return new ResponseEntity<>(
                createErrorResponse(exception.getMessage()),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> entityNotFoundException(EntityNotFoundException exception) {
        return new ResponseEntity<>(
                createErrorResponse(exception.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = EntityAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> entityAlreadyExistsException(EntityAlreadyExistsException exception) {
        return new ResponseEntity<>(
                createErrorResponse(exception.getMessage()),
                HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> accessDeniedException(AccessDeniedException exception) {
        exception.printStackTrace();
        return new ResponseEntity<>(
                createErrorResponse(exception.getMessage()),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = RentException.class)
    public ResponseEntity<ErrorResponse> rentException(RentException exception) {
        return new ResponseEntity<>(
                createErrorResponse(exception.getMessage()),
                HttpStatus.PRECONDITION_FAILED);
    }

    private ErrorResponse createErrorResponse(String errorMessage) {
        return ErrorResponse.builder()
                .message(errorMessage)
                .build();
    }

}
