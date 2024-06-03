package com.ead.course.api.controller;


import com.ead.course.domain.dtos.request.SubscriptionUserIdRequestDTO;
import com.ead.course.domain.dtos.response.CourseUserModelDTO;
import com.ead.course.domain.dtos.response.UserDTO;
import com.ead.course.domain.services.CourseUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/courses/{courseId}/users")
public class CourseUserController {

    private final CourseUserService courseUserService;

    @GetMapping
    public ResponseEntity<Page<UserDTO>> getAllUsersByCourse(
            @PageableDefault(page = 0, size = 10, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable,
            @PathVariable UUID courseId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(courseUserService.getAllUsersByCourse(courseId, pageable));
    }

    @PostMapping("/subscription")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CourseUserModelDTO> saveSubscriptionUserInCourse(
            @PathVariable UUID courseId,
            @RequestBody @Valid SubscriptionUserIdRequestDTO subscriptionUserIdRequestDTO
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseUserService.save(courseId, subscriptionUserIdRequestDTO));
    }

}
