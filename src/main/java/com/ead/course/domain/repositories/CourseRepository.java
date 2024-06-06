package com.ead.course.domain.repositories;

import com.ead.course.domain.models.CourseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<CourseModel, UUID>, JpaSpecificationExecutor<CourseModel> {

    @Query(value = "select case when count(tcu) > 0 THEN true ELSE false END FROM courses_users tcu " +
            "WHERE tcu.course_id= :courseId and tcu.user_id= :userId", nativeQuery = true)
    boolean existsByCourseAndUser(@Param("courseId") UUID courseId, @Param("userId") UUID userId);

    @Modifying
    @Query(value = "insert into courses_users values (:courseId,:userId);", nativeQuery = true)
    void saveCourseUser(@Param("courseId") UUID courseId, @Param("userId") UUID userId);

    @Modifying
    @Query(value = "delete from courses_users where course_id= :courseId", nativeQuery = true)
    void deleteCourseUserByCourseId(@Param("courseId") UUID courseId);

    @Modifying
    @Query(value = "delete from courses_users where user_id= :userId", nativeQuery = true)
    void deleteCourseUserByUserId(UUID userId);
}
