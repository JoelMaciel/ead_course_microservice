package com.ead.course.domain.converter;

import com.ead.course.domain.dtos.request.LessonRequestDTO;
import com.ead.course.domain.dtos.request.LessonUpdateRequestDTO;
import com.ead.course.domain.dtos.response.LessonDTO;
import com.ead.course.domain.models.LessonModel;
import com.ead.course.domain.models.ModuleModel;
import org.springframework.data.domain.Page;

import java.time.OffsetDateTime;


public class LessonConverter {

    private LessonConverter() {
    }

    public static Page<LessonDTO> toDTOPage(Page<LessonModel> lessonModels) {
        return lessonModels.map(LessonConverter::toDTO);

    }

    public static LessonDTO toDTO(LessonModel lessonModel) {
        return LessonDTO.builder()
                .lessonId(lessonModel.getLessonId())
                .title(lessonModel.getTitle())
                .description(lessonModel.getDescription())
                .moduleId(lessonModel.getModule().getModuleId())
                .videoUrl(lessonModel.getVideoUrl())
                .creationDate(OffsetDateTime.now())
                .build();
    }

    public static LessonModel toEntity(LessonRequestDTO lessonRequestDTO, ModuleModel moduleModel) {
        return LessonModel.builder()
                .title(lessonRequestDTO.getTitle())
                .description(lessonRequestDTO.getDescription())
                .module(moduleModel)
                .videoUrl(lessonRequestDTO.getVideoUrl())
                .build();
    }

    public static LessonModel toUpdateEntity(LessonModel lessonModel, LessonUpdateRequestDTO lessonUpdateRequestDTO) {
        return lessonModel.toBuilder()
                .title(lessonUpdateRequestDTO.getTitle())
                .description(lessonUpdateRequestDTO.getDescription())
                .videoUrl(lessonUpdateRequestDTO.getVideoUrl())
                .build();
    }

}
