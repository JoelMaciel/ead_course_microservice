package com.ead.course.domain.repositories;

import com.ead.course.domain.models.LessonModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LessonRepository extends JpaRepository<LessonModel, UUID> {

    @Query(value = "select * from lessons where module_id = :moduleId", nativeQuery = true)
    List<LessonModel> findAllLessonsIntoModules(@Param("moduleId") UUID moduleId);
}