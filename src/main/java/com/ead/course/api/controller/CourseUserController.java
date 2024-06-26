package com.ead.course.api.controller;


import com.ead.course.domain.dtos.request.SubscriptionUserIdRequestDTO;
import com.ead.course.domain.models.UserModel;
import com.ead.course.domain.services.CourseService;
import com.ead.course.domain.services.UserService;
import com.ead.course.domain.specifications.SpecificationTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping
public class CourseUserController {

    public static final String SUBSCRIPTION_CREATED_SUCCESSFULLY = "Subscription created successfully";
    private final UserService userService;
    private final CourseService courseService;

    @PreAuthorize("hasAnyRole('INSTRUCTOR')")
    @GetMapping("/api/courses/{courseId}/users")
    public Page<UserModel> getAllUsersByCourse(
            SpecificationTemplate.UserSpec spec,
            @PageableDefault(page = 0, size = 10, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable,
            @PathVariable UUID courseId
    ) {
        return userService.findAll(SpecificationTemplate.userCourseId(courseId).and(spec), pageable);
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
    @PostMapping("/api/courses/{courseId}/users/subscription")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> saveSubscriptionUserInCourse(
            @PathVariable UUID courseId,
            @RequestBody @Valid SubscriptionUserIdRequestDTO subscriptionUserIdRequestDTO
    ) {
        courseService.saveSubscriptionUserInCourse(courseId, subscriptionUserIdRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(SUBSCRIPTION_CREATED_SUCCESSFULLY);
    }
}
