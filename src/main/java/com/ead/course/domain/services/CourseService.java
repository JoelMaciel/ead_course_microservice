package com.ead.course.domain.services;

import com.ead.course.domain.dtos.request.CourseRequestDTO;
import com.ead.course.domain.dtos.request.CourseUpdateRequestDTO;
import com.ead.course.domain.dtos.response.CourseDTO;
import com.ead.course.domain.models.CourseModel;

import java.util.List;
import java.util.UUID;

public interface CourseService {

    void delete(CourseModel courseModel);

    CourseDTO save(CourseRequestDTO courseRequestDTO);

    CourseModel optionalCourse(UUID courseUd);

    CourseDTO update(UUID courseId, CourseUpdateRequestDTO courseUpdateRequestDTO);

    List<CourseDTO> findAll();

    CourseDTO findById(UUID courseId);
}
