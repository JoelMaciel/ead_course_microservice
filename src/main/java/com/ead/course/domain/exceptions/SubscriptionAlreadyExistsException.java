package com.ead.course.domain.exceptions;

public class SubscriptionAlreadyExistsException extends EntityInUseException {

    public SubscriptionAlreadyExistsException(String message) {
        super(message);
    }
}
