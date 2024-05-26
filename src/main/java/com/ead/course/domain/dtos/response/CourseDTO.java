package com.ead.course.domain.dtos.response;

import com.ead.course.domain.enums.CourseLevel;
import com.ead.course.domain.enums.CourseStatus;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
public class CourseDTO {

    private UUID courseId;
    private String name;
    private String description;
    private CourseStatus courseStatus;
    private CourseLevel courseLevel;
    private UUID userInstructor;
    private String imageUrl;
    private OffsetDateTime creationDate;
    private OffsetDateTime updateDate;
}
