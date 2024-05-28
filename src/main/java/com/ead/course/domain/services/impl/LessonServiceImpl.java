package com.ead.course.domain.services.impl;

import com.ead.course.domain.converter.LessonConverter;
import com.ead.course.domain.dtos.request.LessonRequestDTO;
import com.ead.course.domain.dtos.request.LessonUpdateRequestDTO;
import com.ead.course.domain.dtos.response.LessonDTO;
import com.ead.course.domain.exceptions.LessonIntoModuleNotFoundException;
import com.ead.course.domain.models.LessonModel;
import com.ead.course.domain.models.ModuleModel;
import com.ead.course.domain.repositories.LessonRepository;
import com.ead.course.domain.services.LessonService;
import com.ead.course.domain.services.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class LessonServiceImpl implements LessonService {

    public static final String MSG_LESSON_MODULE_NOT_FOUND = "There is no lesson registered for this module";
    private final LessonRepository lessonRepository;
    private final ModuleService moduleService;

    @Override
    public Page<LessonDTO> findAllLessonsIntoModule(Specification<LessonModel> spec, Pageable pageable) {
        Page<LessonModel> lessonModels = lessonRepository.findAll(spec, pageable);
        return LessonConverter.toDTOPage(lessonModels);
    }

    @Override
    public LessonDTO findById(UUID moduleId, UUID lessonId) {
        LessonModel lessonModel = findLessonIntoModule(moduleId, lessonId);
        return LessonConverter.toDTO(lessonModel);
    }

    @Transactional
    @Override
    public LessonDTO update(UUID moduleId, UUID lessonId, LessonUpdateRequestDTO lessonUpdateRequestDTO) {
        LessonModel lessonModel = findLessonIntoModule(moduleId, lessonId);
        LessonModel lessonUpdated = LessonConverter.toUpdateEntity(lessonModel, lessonUpdateRequestDTO);
        return LessonConverter.toDTO(lessonRepository.save(lessonUpdated));
    }

    @Transactional
    @Override
    public LessonDTO save(UUID moduleId, LessonRequestDTO lessonRequestDTO) {
        ModuleModel moduleModel = moduleService.optionalModuleModel(moduleId);
        LessonModel lesson = LessonConverter.toEntity(lessonRequestDTO, moduleModel);

        return LessonConverter.toDTO(lessonRepository.save(lesson));
    }

    @Transactional
    @Override
    public void delete(UUID moduleId, UUID lessonId) {
        LessonModel lesson = findLessonIntoModule(moduleId, lessonId);
        lessonRepository.delete(lesson);
    }

    @Override
    public LessonModel findLessonIntoModule(UUID moduleId, UUID lessonId) {
        return lessonRepository.findLessonIntoModule(moduleId, lessonId)
                .orElseThrow(() -> new LessonIntoModuleNotFoundException(MSG_LESSON_MODULE_NOT_FOUND));
    }
}
