package com.ead.course.domain.services.impl;

import com.ead.course.domain.repositories.CourseUserRepository;
import com.ead.course.domain.services.CourseUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CourseUserServiceImpl implements CourseUserService {

    private final CourseUserRepository courseUserRepository;
}
