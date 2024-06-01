package com.ead.course.domain.exceptions;

import java.util.UUID;


public class UserNotFoundException extends EntityNotFoundException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(UUID userId) {
        this(String.format("There is no registered user with this id %s", userId));
    }
}
