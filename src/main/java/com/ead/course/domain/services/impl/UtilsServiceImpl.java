package com.ead.course.domain.services.impl;

import com.ead.course.domain.services.UtilsService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UtilsServiceImpl implements UtilsService {

    String REQUEST_URI = "http://localhost:8087";

    @Override
    public String createUrlGetAllUsersByCourse(UUID courseId, Pageable pageable) {
        return "/api/users?courseId=" + courseId + "&page=" + pageable.getPageNumber()
                + "&size=" + pageable.getPageSize() + "&sort=" + pageable.getSort().toString()
                .replaceAll(": ", ",");
    }
}
