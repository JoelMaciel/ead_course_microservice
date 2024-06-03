package com.ead.course.domain.services;

import com.ead.course.domain.dtos.request.SubscriptionUserIdRequestDTO;
import com.ead.course.domain.dtos.response.CourseUserModelDTO;
import com.ead.course.domain.dtos.response.UserDTO;
import com.ead.course.domain.models.CourseModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CourseUserService {

    boolean existsByCourseAndUserId(CourseModel courseModel, UUID userId);

    CourseUserModelDTO save(UUID courseId, SubscriptionUserIdRequestDTO subscriptionUserIdRequestDTO);

    Page<UserDTO> getAllUsersByCourse(UUID courseId, Pageable pageable);
}
