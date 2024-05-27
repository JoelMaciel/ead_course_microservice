package com.ead.course.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LessonIntoModuleNotFoundException extends RuntimeException {

    public LessonIntoModuleNotFoundException(String message) {
        super(message);
    }
}
