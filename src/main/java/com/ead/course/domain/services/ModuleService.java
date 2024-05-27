package com.ead.course.domain.services;

import com.ead.course.domain.dtos.request.ModuleRequestDTO;
import com.ead.course.domain.dtos.response.ModuleDTO;
import com.ead.course.domain.models.ModuleModel;

import java.util.List;
import java.util.UUID;

public interface ModuleService {

    void delete(ModuleModel moduleModel);

    ModuleDTO save(UUID courseId, ModuleRequestDTO moduleRequestDTO);

    ModuleModel findModuleIntoCourse(UUID courseId, UUID moduleId);

    ModuleDTO update(UUID courseId, UUID moduleId, ModuleRequestDTO moduleRequestDTO);

    List<ModuleDTO> findAllByCourse(UUID courseId);

    ModuleDTO findById(UUID courseId, UUID moduleId);

    ModuleModel optionalModuleModel(UUID moduleId);
}
