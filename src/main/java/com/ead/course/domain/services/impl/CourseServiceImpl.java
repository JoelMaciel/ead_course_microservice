package com.ead.course.domain.services.impl;


import com.ead.course.api.config.security.AuthenticationCurrentUserService;
import com.ead.course.api.controller.CourseController;
import com.ead.course.api.publisher.NotificationCommandPublisher;
import com.ead.course.domain.converter.CourseConverter;
import com.ead.course.domain.dtos.request.CourseRequestDTO;
import com.ead.course.domain.dtos.request.CourseUpdateRequestDTO;
import com.ead.course.domain.dtos.request.SubscriptionUserIdRequestDTO;
import com.ead.course.domain.dtos.response.CourseDTO;
import com.ead.course.domain.enums.UserStatus;
import com.ead.course.domain.enums.UserType;
import com.ead.course.domain.exceptions.BusinessException;
import com.ead.course.domain.exceptions.CourseNotFoundException;
import com.ead.course.domain.exceptions.SubscriptionAlreadyExistsException;
import com.ead.course.domain.exceptions.UserBlockedException;
import com.ead.course.domain.models.CourseModel;
import com.ead.course.domain.models.LessonModel;
import com.ead.course.domain.models.ModuleModel;
import com.ead.course.domain.models.UserModel;
import com.ead.course.domain.repositories.CourseRepository;
import com.ead.course.domain.repositories.LessonRepository;
import com.ead.course.domain.repositories.ModuleRepository;
import com.ead.course.domain.services.CourseService;
import com.ead.course.domain.services.UserService;
import com.ead.course.domain.specifications.SpecificationTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
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
    public static final String REGISTERED_FOR_THIS_COURSE = "User already registered for this course";
    public static final String MSG_HE_IS_BLOCKED = "User cannot register because he is blocked.";
    public static final String USER_DOES_NOT_HAVE_PERMISSION = "User current different from userInstructor";
    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;
    private final UserService userService;
    private final NotificationCommandPublisher notificationCommandPublisher;
    private final AuthenticationCurrentUserService authenticationCurrentUserService;

    @Override
    public Page<CourseDTO> findAll(Specification<CourseModel> spec, Pageable pageable, UUID userId) {
        Specification<CourseModel> finalSpec =
                (userId != null) ? SpecificationTemplate.courseUserid(userId).and(spec) : spec;

        Page<CourseModel> courseModels = courseRepository.findAll(finalSpec, pageable);
        Page<CourseDTO> courseDTOPage = CourseConverter.toDTOPage(courseModels);

        addHateoasLinks(courseDTOPage);

        log.info("GET list course {} ", courseModels.getNumberOfElements());
        return courseDTOPage;
    }

    @Override
    public CourseDTO findById(UUID courseId) {
        CourseModel courseModel = optionalCourse(courseId);
        log.info("GET One course {} ", courseModel.toString());

        return CourseConverter.toDTO(courseModel);
    }

    @Transactional
    @Override
    public CourseDTO save(CourseRequestDTO courseRequestDTO) {
        CourseModel courseModel = CourseConverter.toEntity(courseRequestDTO);
        validateUserInstructor(courseRequestDTO.getUserInstructor());

        log.info("POST course saved {} ", courseModel.toString());
        return CourseConverter.toDTO(courseRepository.save(courseModel));
    }

    @Override
    @Transactional
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

        courseRepository.deleteCourseUserByCourseId(courseModel.getCourseId());
        log.info("Deleting CourseUser's relationship with CourseId: {}", courseModel.getCourseId());

        courseRepository.delete(courseModel);
        log.info("DELETE courseId {} ", courseModel.getCourseId());
    }

    @Override
    public boolean existsByCourseAndUser(UUID courseId, UUID userId) {
        return courseRepository.existsByCourseAndUser(courseId, userId);
    }

    @Transactional
    @Override
    public void saveSubscriptionUserInCourse(UUID courseId, SubscriptionUserIdRequestDTO subscriptionUserIdRequestDTO) {
        var course = optionalCourse(courseId);
        var userModel = userService.optionalUser(subscriptionUserIdRequestDTO.getUserId());

        validateCourseAndUser(course, userModel);
        courseRepository.saveCourseUser(courseId, subscriptionUserIdRequestDTO.getUserId());

        sendSubscriptionUserInCourseToNotification(course, userModel);
        log.info("Notification sent successfully");
    }

    @Override
    public void sendSubscriptionUserInCourseToNotification(CourseModel course, UserModel user) {
        try {
            var notificationCommandDTO = CourseConverter.sendPublisherNotificationDTO(course, user);
            notificationCommandPublisher.publishNotificationCommand(notificationCommandDTO);
        } catch (Exception e) {
            log.warn("Error sending notification");
        }
    }

    @Override
    public CourseModel optionalCourse(UUID courseUd) {
        return courseRepository.findById(courseUd)
                .orElseThrow(() -> new CourseNotFoundException(courseUd));
    }

    private void validateCourseAndUser(CourseModel course, UserModel userModel) {
        if (UserStatus.BLOCKED.toString().equals(userModel.getUserStatus())) {
            throw new UserBlockedException(MSG_HE_IS_BLOCKED);
        }

        if (existsByCourseAndUser(course.getCourseId(), userModel.getUserId())) {
            throw new SubscriptionAlreadyExistsException(REGISTERED_FOR_THIS_COURSE);
        }
    }

    private void validateUserInstructor(UUID userInstructor) {
        UUID currentUserId = authenticationCurrentUserService.getCurrentUser().getUserId();

        if (!currentUserId.equals(userInstructor)) {
            throw new AccessDeniedException(USER_DOES_NOT_HAVE_PERMISSION);
        }

        UserModel userModel = userService.optionalUser(userInstructor);
        UserType userType = UserType.valueOf(userModel.getUserType());

        if (userType == UserType.STUDENT) {
            throw new BusinessException(BE_INSTRUCTOR_OR_ADMIN);
        }
    }


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
