package com.ead.course.api.controller;


import com.ead.course.domain.dtos.request.ModuleRequestDTO;
import com.ead.course.domain.dtos.response.ModuleDTO;
import com.ead.course.domain.models.ModuleModel;
import com.ead.course.domain.services.ModuleService;
import com.ead.course.domain.specifications.SpecificationTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/courses/{courseId}/modules")
public class ModuleController {

    private final ModuleService moduleService;

    @GetMapping
    public Page<ModuleDTO> getAllModules(
            @PathVariable UUID courseId, SpecificationTemplate.ModuleSpec spec,
            @PageableDefault(page = 0, size = 10, sort = "moduleId", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return moduleService.findAllByCourse(SpecificationTemplate.moduleCourseId(courseId).and(spec), pageable);
    }

    @GetMapping("/{moduleId}")
    public ModuleDTO getOneModule(@PathVariable UUID courseId, @PathVariable UUID moduleId) {
        return moduleService.findById(courseId, moduleId);
    }

    @PostMapping()
    public ModuleDTO saveModule(@PathVariable UUID courseId, @RequestBody @Valid ModuleRequestDTO moduleRequestDTO) {
        return moduleService.save(courseId, moduleRequestDTO);
    }

    @PutMapping("/{moduleId}")
    public ModuleDTO updateModule(
            @PathVariable UUID courseId, @PathVariable UUID moduleId,
            @RequestBody @Valid ModuleRequestDTO moduleRequestDTO
    ) {
        return moduleService.update(courseId, moduleId, moduleRequestDTO);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{moduleId}")
    public void deleteModule(@PathVariable UUID courseId, @PathVariable UUID moduleId) {
        ModuleModel model = moduleService.findModuleIntoCourse(courseId, moduleId);
        moduleService.delete(model);
    }
}
