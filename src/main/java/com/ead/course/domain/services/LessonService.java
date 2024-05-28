package com.ead.course.domain.services;

import com.ead.course.domain.dtos.request.LessonRequestDTO;
import com.ead.course.domain.dtos.request.LessonUpdateRequestDTO;
import com.ead.course.domain.dtos.response.LessonDTO;
import com.ead.course.domain.models.LessonModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public interface LessonService {

    Page<LessonDTO> findAllLessonsIntoModule(Specification<LessonModel> and, Pageable pageable);

    LessonDTO findById(UUID moduleId, UUID lessonId);

    LessonDTO update(UUID moduleId, UUID lessonId, LessonUpdateRequestDTO lessonUpdateRequestDTO);

    LessonDTO save(UUID moduleId, LessonRequestDTO lessonRequestDTO);

    void delete(UUID moduleId, UUID lessonId);

    LessonModel findLessonIntoModule(UUID moduleId, UUID lessonId);
}
