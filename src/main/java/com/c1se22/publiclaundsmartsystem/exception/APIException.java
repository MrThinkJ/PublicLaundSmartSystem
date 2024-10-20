package com.c1se22.publiclaundsmartsystem.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
@Getter
public class APIException extends RuntimeException{
    private final HttpStatus status;
    private final String message;

    public APIException(HttpStatus status, String message){
        this.status = status;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
