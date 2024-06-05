package com.ead.course.domain.services.impl;


import com.ead.course.api.controller.CourseController;
import com.ead.course.domain.converter.CourseConverter;
import com.ead.course.domain.dtos.request.CourseRequestDTO;
import com.ead.course.domain.dtos.request.CourseUpdateRequestDTO;
import com.ead.course.domain.dtos.response.CourseDTO;
import com.ead.course.domain.exceptions.CourseNotFoundException;
import com.ead.course.domain.models.CourseModel;
import com.ead.course.domain.models.LessonModel;
import com.ead.course.domain.models.ModuleModel;
import com.ead.course.domain.repositories.CourseRepository;
import com.ead.course.domain.repositories.LessonRepository;
import com.ead.course.domain.repositories.ModuleRepository;
import com.ead.course.domain.repositories.UserRepository;
import com.ead.course.domain.services.CourseService;
import com.ead.course.domain.services.UserService;
import com.ead.course.domain.specifications.SpecificationTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Log4j2
@RequiredArgsConstructor
@Service
public class CourseServiceImpl implements CourseService {

    public static final String BE_INSTRUCTOR_OR_ADMIN = "To save a course you need to be INSTRUCTOR OR ADMIN";
    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public Page<CourseDTO> findAll(Specification<CourseModel> spec, Pageable pageable, UUID userId) {
        Specification<CourseModel> finalSpec =
                (userId != null) ? SpecificationTemplate.courseUserid(userId).and(spec) : spec;

        Page<CourseModel> courseModels = courseRepository.findAll(finalSpec, pageable);
        Page<CourseDTO> courseDTOPage = CourseConverter.toDTOPage(courseModels);

        addHateoasLinks(courseDTOPage);

        log.debug("GET list course {} ", courseModels.getNumberOfElements());
        return courseDTOPage;
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
        //  validateUserInstructor(courseRequestDTO.getUserInstructor());
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
        log.debug("DELETE courseId {} ", courseModel.getCourseId());
    }

    @Override
    public CourseModel optionalCourse(UUID courseUd) {
        return courseRepository.findById(courseUd)
                .orElseThrow(() -> new CourseNotFoundException(courseUd));
    }

//    private void validateUserInstructor(UUID userInstructor) {
//        try {
//            if (userType.equals(UserType.STUDENT)) {
//                throw new BusinessException(BE_INSTRUCTOR_OR_ADMIN);
//            }
//        } catch (ResponseStatusException e) {
//            if (e.getStatus().equals(HttpStatus.NOT_FOUND)) {
//                throw new UserNotFoundException(userInstructor);
//            }
//        }
//    }

    private void addHateoasLinks(Page<CourseDTO> courseDTOS) {
        if (!courseDTOS.isEmpty()) {
            for (CourseDTO courseDTO : courseDTOS) {
                courseDTO.add(linkTo(methodOn(CourseController.class).getOneCourse(courseDTO.getCourseId())).withSelfRel());
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
