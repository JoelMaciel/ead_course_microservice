package com.ead.course.domain.services;

import com.ead.course.domain.models.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public interface UserService {

    UserModel save(UserModel userModel);

    UserModel optionalUser(UUID userId);

    Page<UserModel> findAll(Specification<UserModel> spec, Pageable pageable);
}
