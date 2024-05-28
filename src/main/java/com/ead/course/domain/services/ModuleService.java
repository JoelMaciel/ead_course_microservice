package com.ead.course.domain.services;

import com.ead.course.domain.dtos.request.ModuleRequestDTO;
import com.ead.course.domain.dtos.response.ModuleDTO;
import com.ead.course.domain.models.ModuleModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public interface ModuleService {

    Page<ModuleDTO> findAllByCourse(Specification<ModuleModel> spec, Pageable pageable);

    ModuleDTO findById(UUID courseId, UUID moduleId);

    ModuleDTO update(UUID courseId, UUID moduleId, ModuleRequestDTO moduleRequestDTO);

    ModuleDTO save(UUID courseId, ModuleRequestDTO moduleRequestDTO);

    void delete(ModuleModel moduleModel);

    ModuleModel findModuleIntoCourse(UUID courseId, UUID moduleId);

    ModuleModel optionalModuleModel(UUID moduleId);
}
