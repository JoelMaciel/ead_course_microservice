package com.ead.course.api.controller;


import com.ead.course.domain.dtos.request.LessonRequestDTO;
import com.ead.course.domain.dtos.request.LessonUpdateRequestDTO;
import com.ead.course.domain.dtos.response.LessonDTO;
import com.ead.course.domain.services.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/modules/{moduleId}/lessons")
public class LessonController {

    private final LessonService lessonService;

    @GetMapping
    public List<LessonDTO> getAllLessons(@PathVariable UUID moduleId) {
        return lessonService.findAllLessonsIntoModule(moduleId);

    }

    @GetMapping("/{lessonId}")
    public LessonDTO getOneLesson(@PathVariable UUID moduleId, @PathVariable UUID lessonId) {
        return lessonService.findById(moduleId, lessonId);
    }

    @PutMapping("/{lessonId}")
    public LessonDTO updateLesson(
            @PathVariable UUID moduleId, @PathVariable UUID lessonId,
            @RequestBody @Valid LessonUpdateRequestDTO lessonUpdateRequestDTO
    ) {
        return lessonService.update(moduleId, lessonId, lessonUpdateRequestDTO);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LessonDTO saveLesson(@PathVariable UUID moduleId, @RequestBody @Valid LessonRequestDTO lessonRequestDTO) {
        return lessonService.save(moduleId, lessonRequestDTO);
    }

    @DeleteMapping("/{lessonId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLesson(@PathVariable UUID moduleId, @PathVariable UUID lessonId) {
        lessonService.delete(moduleId, lessonId);
    }
}