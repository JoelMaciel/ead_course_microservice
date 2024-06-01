package com.ead.course.domain.exceptions;

import java.util.UUID;

public class CourseNotFoundException extends EntityNotFoundException {

    public CourseNotFoundException(String message) {
        super(message);
    }

    public CourseNotFoundException(UUID courseId) {
        this(String.format("Course not found for id %s ", courseId));
    }

}
