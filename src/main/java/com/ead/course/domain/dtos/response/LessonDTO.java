package com.ead.course.domain.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
@Data
public class LessonDTO {

    private UUID lessonId;
    private String title;
    private String description;
    private String videoUrl;
    private UUID moduleId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private OffsetDateTime creationDate;

}
