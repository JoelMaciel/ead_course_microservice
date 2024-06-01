package com.ead.course.domain.services.impl;


import com.ead.course.api.clients.AuthUserClient;
import com.ead.course.domain.converter.CourseConverter;
import com.ead.course.domain.dtos.request.CourseRequestDTO;
import com.ead.course.domain.dtos.request.CourseUpdateRequestDTO;
import com.ead.course.domain.dtos.response.CourseDTO;
import com.ead.course.domain.dtos.response.UserDTO;
import com.ead.course.domain.enums.UserType;
import com.ead.course.domain.exceptions.BusinessException;
import com.ead.course.domain.exceptions.CourseNotFoundException;
import com.ead.course.domain.exceptions.UserNotFoundException;
import com.ead.course.domain.models.CourseModel;
import com.ead.course.domain.models.LessonModel;
import com.ead.course.domain.models.ModuleModel;
import com.ead.course.domain.repositories.CourseRepository;
import com.ead.course.domain.repositories.LessonRepository;
import com.ead.course.domain.repositories.ModuleRepository;
import com.ead.course.domain.services.CourseService;
import com.ead.course.domain.specifications.SpecificationTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@Service
public class CourseServiceImpl implements CourseService {

    public static final String BE_INSTRUCTOR_OR_ADMIN = "To save a course you need to be INSTRUCTOR OR ADMIN";
    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;
    private final AuthUserClient authUserClient;

    @Override
    public Page<CourseDTO> findAll(Specification<CourseModel> spec, Pageable pageable, UUID userId) {
        Specification<CourseModel> finalSpec = createSpecificationWithUserId(spec, userId);

        Page<CourseModel> courseModels = courseRepository.findAll(finalSpec, pageable);
        log.debug("GET list course {} ", courseModels.toString());
        return CourseConverter.toDTOPage(courseModels);
    }

    private Specification<CourseModel> createSpecificationWithUserId(Specification<CourseModel> spec, UUID userId) {
        return (userId != null) ? SpecificationTemplate.courseUserId(userId).and(spec) : spec;
    }

    @Override
    public CourseDTO findById(UUID courseId) {
        CourseModel courseModel = optionalCourse(courseId);
        log.debug("GET One course {} ", courseModel.toString());
        return CourseConverter.toDTO(courseModel);
    }

    @Transactional
    @Override
    public CourseDTO save(CourseRequestDTO courseRequestDTO) {
        CourseModel courseModel = CourseConverter.toEntity(courseRequestDTO);
        validateUserInstructor(courseRequestDTO.getUserInstructor());
        log.debug("POST course saved {} ", courseModel.toString());
        return CourseConverter.toDTO(courseRepository.save(courseModel));
    }

    @Override
    public CourseDTO update(UUID courseId, CourseUpdateRequestDTO courseUpdateRequestDTO) {
        CourseModel courseModel = optionalCourse(courseId);
        CourseModel courseUpdated = CourseConverter.toUpdateEntity(courseModel, courseUpdateRequestDTO);
        log.debug("PATCH course updated {} ", courseUpdated.toString());
        return CourseConverter.toDTO(courseRepository.save(courseUpdated));
    }

    @Transactional
    @Override
    public void delete(CourseModel courseModel) {
        List<ModuleModel> moduleList = moduleRepository.findAllModulesIntoCourse(courseModel.getCourseId());
        deleteModulesAndLessons(moduleList);
        courseRepository.delete(courseModel);
        log.debug("DELETE Module and Lesson {} ", moduleList.toString());
        log.debug("DELETE course {} ", courseModel.toString());
    }

    @Override
    public CourseModel optionalCourse(UUID courseUd) {
        return courseRepository.findById(courseUd)
                .orElseThrow(() -> new CourseNotFoundException(courseUd));
    }

    private void validateUserInstructor(UUID userInstructor) {
        ResponseEntity<UserDTO> userDTO;
        try {
            userDTO = authUserClient.getOneUserById(userInstructor);
            log.debug("UserDTO -> UserType {} ", userDTO.getBody().getUserType());
            UserType userType = UserType.valueOf(userDTO.getBody().getUserType());

            if (userType.equals(UserType.STUDENT)) {
                throw new BusinessException(BE_INSTRUCTOR_OR_ADMIN);
            }
        } catch (ResponseStatusException e) {
            if (e.getStatus().equals(HttpStatus.NOT_FOUND)) {
                throw new UserNotFoundException(userInstructor);
            }
        }
    }

    private void deleteModulesAndLessons(List<ModuleModel> moduleList) {
        if (!moduleList.isEmpty()) {
            for (ModuleModel module : moduleList) {
                List<LessonModel> lessonModelList = lessonRepository.findAllLessonsIntoModules(module.getModuleId());
                if (!lessonModelList.isEmpty()) {
                    lessonRepository.deleteAll(lessonModelList);
                }
            }
            moduleRepository.deleteAll(moduleList);
        }
    }
}
