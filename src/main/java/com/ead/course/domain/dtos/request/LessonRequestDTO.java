package com.ead.course.domain.dtos.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LessonRequestDTO {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    private String videoUrl;

}
