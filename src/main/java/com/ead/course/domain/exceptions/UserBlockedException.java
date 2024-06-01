package com.ead.course.domain.exceptions;

public class UserBlockedException extends EntityNotFoundException {

    public UserBlockedException(String message) {
        super(message);
    }
}
