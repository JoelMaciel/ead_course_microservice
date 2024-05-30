package com.ead.course.domain.services;

import com.ead.course.domain.dtos.request.CourseRequestDTO;
import com.ead.course.domain.dtos.request.CourseUpdateRequestDTO;
import com.ead.course.domain.dtos.response.CourseDTO;
import com.ead.course.domain.models.CourseModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public interface CourseService {

    void delete(CourseModel courseModel);

    CourseDTO save(CourseRequestDTO courseRequestDTO);

    CourseModel optionalCourse(UUID courseUd);

    CourseDTO update(UUID courseId, CourseUpdateRequestDTO courseUpdateRequestDTO);

    Page<CourseDTO> findAll(Specification<CourseModel> spec, Pageable pageable, UUID userId);

    CourseDTO findById(UUID courseId);
}
