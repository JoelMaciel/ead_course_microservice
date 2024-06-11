package com.ead.course.domain.dtos.request;

import lombok.*;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotificationCommandDTO {

    private String title;
    private String message;
    private UUID userId;
}
