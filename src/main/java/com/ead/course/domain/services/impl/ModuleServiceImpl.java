package com.ead.course.domain.services.impl;

import com.ead.course.domain.converter.ModuleConverter;
import com.ead.course.domain.dtos.request.ModuleRequestDTO;
import com.ead.course.domain.dtos.response.ModuleDTO;
import com.ead.course.domain.exceptions.ModuleCourseNotFoundException;
import com.ead.course.domain.exceptions.ModuleNotFoundException;
import com.ead.course.domain.models.CourseModel;
import com.ead.course.domain.models.LessonModel;
import com.ead.course.domain.models.ModuleModel;
import com.ead.course.domain.repositories.LessonRepository;
import com.ead.course.domain.repositories.ModuleRepository;
import com.ead.course.domain.services.CourseService;
import com.ead.course.domain.services.ModuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@Service
public class ModuleServiceImpl implements ModuleService {

    public static final String MSG_MODULE_COURSE_NOT_FOUND = "Module not found for this course";
    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;
    private final CourseService courseService;

    @Override
    public Page<ModuleDTO> findAllByCourse(Specification<ModuleModel> spec, Pageable pageable) {
        Page<ModuleModel> moduleModelPage = moduleRepository.findAll(spec, pageable);
        log.debug("GET All Modules {} ", moduleModelPage.toString());
        return ModuleConverter.toDTOPage(moduleModelPage);
    }

    @Override
    public ModuleDTO findById(UUID courseId, UUID moduleId) {
        ModuleModel module = findModuleIntoCourse(courseId, moduleId);
        log.debug("GET One Module {} ", module.toString());
        return ModuleConverter.toDTO(module);
    }

    @Transactional
    @Override
    public ModuleDTO save(UUID courseId, ModuleRequestDTO moduleRequestDTO) {
        CourseModel courseModel = courseService.optionalCourse(courseId);
        ModuleModel module = ModuleConverter.toEntity(moduleRequestDTO, courseModel);
        ModuleModel savedModule = moduleRepository.save(module);
        log.debug("POST Module saved {} ", savedModule.toString());
        return ModuleConverter.toDTO(savedModule);
    }

    @Transactional
    @Override
    public ModuleDTO update(UUID courseId, UUID moduleId, ModuleRequestDTO moduleRequestDTO) {
        ModuleModel model = findModuleIntoCourse(courseId, moduleId);

        ModuleModel moduleUpdated = ModuleConverter.toUpdateEntity(model, moduleRequestDTO);
        log.debug("PUT Module updated {} ", moduleUpdated.toString());
        return ModuleConverter.toDTO(moduleRepository.save(moduleUpdated));
    }

    @Transactional
    @Override
    public void delete(ModuleModel moduleModel) {
        List<LessonModel> lessonModels = lessonRepository.findAllLessonsIntoModules(moduleModel.getModuleId());
        if (!lessonModels.isEmpty()) {
            lessonRepository.deleteAll(lessonModels);
        }
        moduleRepository.delete(moduleModel);
    }

    @Override
    public ModuleModel optionalModuleModel(UUID moduleId) {
        return moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ModuleNotFoundException(moduleId));
    }

    @Override
    public ModuleModel findModuleIntoCourse(UUID courseId, UUID moduleId) {
        return moduleRepository.findModuleIntoCourse(courseId, moduleId)
                .orElseThrow(() -> new ModuleCourseNotFoundException(MSG_MODULE_COURSE_NOT_FOUND));
    }
}
