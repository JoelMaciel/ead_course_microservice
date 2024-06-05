package com.ead.course.domain.services.impl;

import com.ead.course.domain.exceptions.UserNotFoundException;
import com.ead.course.domain.models.UserModel;
import com.ead.course.domain.repositories.UserRepository;
import com.ead.course.domain.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.LinkOption;
import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Page<UserModel> findAll(Specification<UserModel> spec, Pageable pageable) {
        return userRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional
    public void delete(UUID userId) {
        userRepository.deleteById(userId);
        log.info("UserId deleted {} -> ", userId);
    }

    @Override
    @Transactional
    public UserModel save(UserModel userModel) {
        log.info("TO SAVE {} :", userModel);
        return userRepository.save(userModel);
    }

    @Override
    public UserModel optionalUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}
