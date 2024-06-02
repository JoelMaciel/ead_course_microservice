package com.ead.course.domain.dtos.request;

import com.ead.course.domain.enums.CourseLevel;
import com.ead.course.domain.enums.CourseStatus;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CourseUpdateRequestDTO {

    @NotBlank
    @Size(min = 8, max = 60)
    private String name;

    @NotBlank
    @Size(min = 15, max = 100)
    private String description;

    @NotBlank
    private String imageUrl;

    @NotNull
    private CourseStatus courseStatus;

    @NotNull
    private CourseLevel courseLevel;

}
