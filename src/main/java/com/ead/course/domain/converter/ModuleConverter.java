package com.ead.course.domain.converter;

import com.ead.course.domain.dtos.request.ModuleRequestDTO;
import com.ead.course.domain.dtos.response.ModuleDTO;
import com.ead.course.domain.models.CourseModel;
import com.ead.course.domain.models.ModuleModel;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;


public class ModuleConverter {

    private ModuleConverter() {
    }

    public static List<ModuleDTO> toDTOList(List<ModuleModel> moduleModels) {
        return moduleModels.stream()
                .map(ModuleConverter::toDTO)
                .collect(Collectors.toList());
    }

    public static ModuleDTO toDTO(ModuleModel moduleModel) {
        return ModuleDTO.builder()
                .moduleId(moduleModel.getModuleId())
                .title(moduleModel.getTitle())
                .description(moduleModel.getDescription())
                .courseId(moduleModel.getCourse().getCourseId())
                .creationDate(OffsetDateTime.now())
                .build();
    }

    public static ModuleModel toEntity(ModuleRequestDTO moduleRequestDTO, CourseModel courseModel) {
        return ModuleModel.builder()
                .title(moduleRequestDTO.getTitle())
                .description(moduleRequestDTO.getDescription())
                .course(courseModel)
                .creationDate(OffsetDateTime.now())
                .build();
    }

    public static ModuleModel toUpdateEntity(ModuleModel model, ModuleRequestDTO moduleRequestDTO) {
        return model.toBuilder()
                .title(moduleRequestDTO.getTitle())
                .description(moduleRequestDTO.getDescription())
                .build();
    }

}
