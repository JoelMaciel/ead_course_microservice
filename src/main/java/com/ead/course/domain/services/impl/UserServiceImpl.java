package com.ead.course.domain.services.impl;

import com.ead.course.domain.repositories.UserRepository;
import com.ead.course.domain.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

}
