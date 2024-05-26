package com.ead.course.api.controller;


import com.ead.course.domain.dtos.request.CourseRequestDTO;
import com.ead.course.domain.dtos.request.CourseUpdateRequestDTO;
import com.ead.course.domain.dtos.response.CourseDTO;
import com.ead.course.domain.models.CourseModel;
import com.ead.course.domain.services.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public List<CourseDTO> getAllCourses() {
        return courseService.findAll();
    }

    @GetMapping("/{courseId}")
    public CourseDTO getOneCourse(@PathVariable UUID courseId) {
        return courseService.findById(courseId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourseDTO saveCourse(@RequestBody @Valid CourseRequestDTO courseRequestDTO) {
        return courseService.save(courseRequestDTO);
    }

    @PatchMapping("/{courseId}")
    public CourseDTO updateCourse(
            @PathVariable UUID courseId,
            @RequestBody @Valid CourseUpdateRequestDTO courseUpdateRequestDTO
    ) {
        return courseService.update(courseId, courseUpdateRequestDTO);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{courseId}")
    public void deleteCourse(@PathVariable UUID courseId) {
        CourseModel courseModel = courseService.optionalCourse(courseId);
        courseService.delete(courseModel);
    }

}
