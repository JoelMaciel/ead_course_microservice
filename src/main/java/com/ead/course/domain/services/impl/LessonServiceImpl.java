package com.ead.course.domain.services.impl;

import com.ead.course.domain.repositories.LessonRepository;
import com.ead.course.domain.services.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
}
