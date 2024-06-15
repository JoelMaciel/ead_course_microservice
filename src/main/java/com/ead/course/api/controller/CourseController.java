package com.ead.course.api.controller;


import com.ead.course.domain.dtos.request.CourseRequestDTO;
import com.ead.course.domain.dtos.request.CourseUpdateRequestDTO;
import com.ead.course.domain.dtos.response.CourseDTO;
import com.ead.course.domain.models.CourseModel;
import com.ead.course.domain.services.CourseService;
import com.ead.course.domain.specifications.SpecificationTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    @PreAuthorize("hasAnyRole('STUDENT')")
    @GetMapping
    public Page<CourseDTO> getAllCourses(
            SpecificationTemplate.CourseSpec spec, @PageableDefault(page = 0, size = 10,
            sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) UUID userId
    ) {
        return courseService.findAll(spec, pageable, userId);
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
    @GetMapping("/{courseId}")
    public CourseDTO getOneCourse(@PathVariable UUID courseId) {
        return courseService.findById(courseId);
    }

    @PreAuthorize("hasAnyRole('INSTRUCTOR')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourseDTO saveCourse(@RequestBody @Valid CourseRequestDTO courseRequestDTO) {
        return courseService.save(courseRequestDTO);
    }

    @PreAuthorize("hasAnyRole('INSTRUCTOR')")
    @PatchMapping("/{courseId}")
    public CourseDTO updateCourse(
            @PathVariable UUID courseId,
            @RequestBody @Valid CourseUpdateRequestDTO courseUpdateRequestDTO
    ) {
        return courseService.update(courseId, courseUpdateRequestDTO);
    }

    @PreAuthorize("hasAnyRole('INSTRUCTOR')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{courseId}")
    public void deleteCourse(@PathVariable UUID courseId) {
        CourseModel courseModel = courseService.optionalCourse(courseId);
        courseService.delete(courseModel);
    }

}
