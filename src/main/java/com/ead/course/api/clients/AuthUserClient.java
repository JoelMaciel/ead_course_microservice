package com.ead.course.api.clients;

import com.ead.course.domain.dtos.request.UserCourseRequestDTO;
import com.ead.course.domain.dtos.response.ResponsePageDTO;
import com.ead.course.domain.dtos.response.UserDTO;
import com.ead.course.domain.services.UtilsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;


@Log4j2
@RequiredArgsConstructor
@Component
public class AuthUserClient {

    private final RestTemplate restTemplate;
    private final UtilsService utilsService;

    @Value("${ead.api.url.authuser}")
    String REQUEST_URL_AUTHUSER;

    public Page<UserDTO> getAllUsersByCourse(UUID courseId, Pageable pageable) {
        List<UserDTO> searchResult = null;
        ResponseEntity<ResponsePageDTO<UserDTO>> result = null;

        String url = REQUEST_URL_AUTHUSER + utilsService.createUrlGetAllUsersByCourse(courseId, pageable);
        log.debug("Request URL: {} ", url);
        log.info("Request URL: {} ", url);

        try {
            ParameterizedTypeReference<ResponsePageDTO<UserDTO>> responseType =
                    new ParameterizedTypeReference<ResponsePageDTO<UserDTO>>() {
                    };
            result = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
            searchResult = result.getBody().getContent();
            log.debug("Response Number of Elements: {} ", searchResult.size());
        } catch (HttpStatusCodeException e) {
            log.error("Error request /users {} ", e);
        }

        log.info("Ending request /users courseId {} ", courseId);
        return result.getBody();
    }

    public ResponseEntity<UserDTO> getOneUserById(UUID userId) {
        String url = REQUEST_URL_AUTHUSER + "/api/users/" + userId;
        log.debug("Request URL: {} ", url);
        return restTemplate.exchange(url, HttpMethod.GET, null, UserDTO.class);
    }

    public void postSubscriptionUserInCourse(UUID courseId, UUID userId) {
        String url = REQUEST_URL_AUTHUSER + "/api/users/" + userId + "/courses/subscription";
        UserCourseRequestDTO userCourseRequestDTO = toDTO(courseId, userId);
        restTemplate.postForObject(url, userCourseRequestDTO, String.class);
    }

    private UserCourseRequestDTO toDTO(UUID courseId, UUID userId) {
        return UserCourseRequestDTO.builder()
                .courseId(courseId)
                .userId(userId)
                .build();
    }
}
