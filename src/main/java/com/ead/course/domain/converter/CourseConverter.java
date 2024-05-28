package com.ead.course.domain.converter;

import com.ead.course.domain.dtos.request.CourseRequestDTO;
import com.ead.course.domain.dtos.request.CourseUpdateRequestDTO;
import com.ead.course.domain.dtos.response.CourseDTO;
import com.ead.course.domain.models.CourseModel;
import org.springframework.data.domain.Page;

import java.time.OffsetDateTime;


public class CourseConverter {

    private CourseConverter() {
    }

    public static Page<CourseDTO> toDTOPage(Page<CourseModel> courseModels) {
        return courseModels.map(CourseConverter::toDTO);
    }

    public static CourseDTO toDTO(CourseModel courseModel) {
        return CourseDTO.builder()
                .courseId(courseModel.getCourseId())
                .name(courseModel.getName())
                .description(courseModel.getDescription())
                .courseLevel(courseModel.getCourseLevel())
                .courseStatus(courseModel.getCourseStatus())
                .userInstructor(courseModel.getUserInstructor())
                .imageUrl(courseModel.getImageUrl())
                .creationDate(OffsetDateTime.now())
                .updateDate(OffsetDateTime.now())
                .build();
    }

    public static CourseModel toEntity(CourseRequestDTO courseRequestDTO) {
        return CourseModel.builder()
                .name(courseRequestDTO.getName())
                .description(courseRequestDTO.getDescription())
                .courseLevel(courseRequestDTO.getCourseLevel())
                .courseStatus(courseRequestDTO.getCourseStatus())
                .imageUrl(courseRequestDTO.getImageUrl())
                .userInstructor(courseRequestDTO.getUserInstructor())
                .build();
    }

    public static CourseModel toUpdateEntity(CourseModel courseModel, CourseUpdateRequestDTO courseUpdateRequestDTO) {
        return courseModel.toBuilder()
                .name(courseUpdateRequestDTO.getName())
                .description(courseUpdateRequestDTO.getDescription())
                .imageUrl(courseUpdateRequestDTO.getImageUrl())
                .courseStatus(courseUpdateRequestDTO.getCourseStatus())
                .courseLevel(courseUpdateRequestDTO.getCourseLevel())
                .updateDate(OffsetDateTime.now())
                .build();
    }

}
