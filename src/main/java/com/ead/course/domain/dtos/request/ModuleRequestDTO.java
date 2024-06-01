package com.ead.course.domain.dtos.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ModuleRequestDTO {

    @NotBlank
    @Size(min = 8, max = 50)
    private String title;

    @NotBlank
    @Size(min = 15, max = 50)
    private String description;
}
