package com.ead.course.domain.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CourseUserModelDTO {

    private UUID id;
    private UUID userId;
    private CourseDTO course;
}

