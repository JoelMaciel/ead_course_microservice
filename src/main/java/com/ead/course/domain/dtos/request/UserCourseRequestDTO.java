package com.ead.course.domain.dtos.request;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserCourseRequestDTO {

    private UUID userId;
    private UUID courseId;
}
