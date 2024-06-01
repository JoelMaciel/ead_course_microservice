package com.ead.course.domain.services.impl;

import com.ead.course.api.clients.AuthUserClient;
import com.ead.course.domain.converter.CourseConverter;
import com.ead.course.domain.dtos.request.SubscriptionUserIdRequestDTO;
import com.ead.course.domain.dtos.response.CourseUserModelDTO;
import com.ead.course.domain.dtos.response.UserDTO;
import com.ead.course.domain.enums.UserStatus;
import com.ead.course.domain.exceptions.SubscriptionAlreadyExistsException;
import com.ead.course.domain.exceptions.UserBlockedException;
import com.ead.course.domain.exceptions.UserNotFoundException;
import com.ead.course.domain.models.CourseModel;
import com.ead.course.domain.models.CourseUserModel;
import com.ead.course.domain.repositories.CourseUserRepository;
import com.ead.course.domain.services.CourseService;
import com.ead.course.domain.services.CourseUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CourseUserServiceImpl implements CourseUserService {

    public static final String MSG_USER_ALREADY_ENROLLED = "User already enrolled in this course";
    public static final String THE_USER_IS_BLOCKED = "The user is blocked";
    private final CourseUserRepository courseUserRepository;
    private final CourseService courseService;
    private final AuthUserClient authUserClient;

    @Override
    public boolean existsByCourseAndUserId(CourseModel courseModel, UUID userId) {
        return courseUserRepository.existsByCourseAndUserId(courseModel, userId);
    }

    @Override
    @Transactional
    public CourseUserModelDTO save(UUID courseId, SubscriptionUserIdRequestDTO subscriptionUserIdRequestDTO) {
        ResponseEntity<UserDTO> responseUser;
        CourseModel courseModel = courseService.optionalCourse(courseId);

        validateSubscription(courseModel, subscriptionUserIdRequestDTO);
        CourseUserModel courseUserModel = toEntity(courseModel, subscriptionUserIdRequestDTO.getUserId());

        try {
            responseUser = authUserClient.getOneUserById(subscriptionUserIdRequestDTO.getUserId());
            if (responseUser.getBody().getUserStatus().equals(UserStatus.BLOCKED)) {
                throw new UserBlockedException(THE_USER_IS_BLOCKED);
            }

        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new UserNotFoundException(subscriptionUserIdRequestDTO.getUserId());
            }
        }
        CourseUserModel courseUserModelSaved = courseUserRepository.save(courseUserModel);
        authUserClient.postSubscriptionUserInCourse(
                courseUserModelSaved.getCourse().getCourseId(), courseUserModelSaved.getUserId()
        );
        return toDTO(courseUserModelSaved);
    }

    private CourseUserModel toEntity(CourseModel courseModel, UUID userId) {
        return CourseUserModel.builder()
                .course(courseModel)
                .userId(userId)
                .build();
    }

    private CourseUserModelDTO toDTO(CourseUserModel courseUserModel) {
        return CourseUserModelDTO.builder()
                .id(courseUserModel.getId())
                .course(CourseConverter.toDTO(courseUserModel.getCourse()))
                .userId(courseUserModel.getUserId())
                .build();
    }

    private void validateSubscription(CourseModel courseModel, SubscriptionUserIdRequestDTO subscriptionUserIdRequestDTO) {
        if (existsByCourseAndUserId(courseModel, subscriptionUserIdRequestDTO.getUserId())) {
            throw new SubscriptionAlreadyExistsException(MSG_USER_ALREADY_ENROLLED);
        }
    }
}
