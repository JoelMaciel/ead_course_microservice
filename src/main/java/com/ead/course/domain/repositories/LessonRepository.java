package com.ead.course.domain.repositories;

import com.ead.course.domain.models.LessonModel;
import com.ead.course.domain.models.ModuleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LessonRepository extends JpaRepository<LessonModel, UUID> {
}
