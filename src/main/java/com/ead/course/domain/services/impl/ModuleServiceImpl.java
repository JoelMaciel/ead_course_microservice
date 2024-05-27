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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ModuleServiceImpl implements ModuleService {

    public static final String MSG_MODULE_COURSE_NOT_FOUND = "Module not found for this course";
    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;
    private final CourseService courseService;

    @Override
    public List<ModuleDTO> findAllByCourse(UUID courseId) {
        List<ModuleModel> moduleModelList = moduleRepository.findAllModulesIntoCourse(courseId);
        return ModuleConverter.toDTOList(moduleModelList);
    }

    @Override
    public ModuleDTO findById(UUID courseId, UUID moduleId) {
        ModuleModel module = findModuleIntoCourse(courseId, moduleId);
        return ModuleConverter.toDTO(module);
    }

    @Transactional
    @Override
    public ModuleDTO save(UUID courseId, ModuleRequestDTO moduleRequestDTO) {
        CourseModel courseModel = courseService.optionalCourse(courseId);
        ModuleModel module = ModuleConverter.toEntity(moduleRequestDTO, courseModel);
        ModuleModel savedModule = moduleRepository.save(module);
        return ModuleConverter.toDTO(savedModule);
    }

    @Transactional
    @Override
    public ModuleDTO update(UUID courseId, UUID moduleId, ModuleRequestDTO moduleRequestDTO) {
        ModuleModel model = findModuleIntoCourse(courseId, moduleId);

        ModuleModel modelUpdated = ModuleConverter.toUpdateEntity(model, moduleRequestDTO);
        return ModuleConverter.toDTO(moduleRepository.save(modelUpdated));
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
