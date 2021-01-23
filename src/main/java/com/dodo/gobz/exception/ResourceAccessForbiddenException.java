package com.dodo.gobz.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ResourceAccessForbiddenException extends RuntimeException {

    public ResourceAccessForbiddenException(String resourceName, String cause) {
        super(String.format("%s can't be accessed because %s", resourceName, cause));
    }
}
