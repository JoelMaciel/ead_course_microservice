package com.ead.course.domain.models;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.UUID;

@Builder
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "COURSES_USERS")
public class CourseUserModel {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @JoinColumn(name = "course_id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private CourseModel course;

    @Column(nullable = false)
    private UUID userId;
}


