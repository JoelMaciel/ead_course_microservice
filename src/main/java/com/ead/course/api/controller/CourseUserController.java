package com.ead.course.api.controller;


import com.ead.course.api.clients.UserClient;
import com.ead.course.domain.dtos.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class CourseUserController {

    private final UserClient userClient;

    @GetMapping("/api/courses/{courseId}/users")
    public ResponseEntity<Page<UserDTO>> getAllUsersByCourse(
            @PageableDefault(page = 0, size = 10, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable,
            @PathVariable UUID courseId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(userClient.getAllUsersByCourse(courseId, pageable));
    }

}
