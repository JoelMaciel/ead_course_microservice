package com.ead.course.domain.exceptions;

import java.util.UUID;

public class ModuleNotFoundException extends RuntimeException {

    public ModuleNotFoundException(String message) {
        super(message);
    }

    public ModuleNotFoundException(UUID moduleId) {
        this(String.format("There is no module registered with this id %s ", moduleId));
    }
}
