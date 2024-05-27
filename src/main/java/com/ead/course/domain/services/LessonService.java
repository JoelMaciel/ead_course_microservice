package com.ead.course.domain.services;

import com.ead.course.domain.dtos.request.LessonRequestDTO;
import com.ead.course.domain.dtos.request.LessonUpdateRequestDTO;
import com.ead.course.domain.dtos.response.LessonDTO;
import com.ead.course.domain.models.LessonModel;

import java.util.List;
import java.util.UUID;

public interface LessonService {

    LessonDTO save(UUID moduleId, LessonRequestDTO lessonRequestDTO);

    void delete(UUID moduleId, UUID lessonId);

    LessonModel findLessonIntoModule(UUID moduleId, UUID lessonId);

    LessonDTO update(UUID moduleId, UUID lessonId, LessonUpdateRequestDTO lessonUpdateRequestDTO);

    List<LessonDTO> findAllLessonsIntoModule(UUID moduleId);

    LessonDTO findById(UUID moduleId, UUID lessonId);
}
