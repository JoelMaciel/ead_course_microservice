package com.ead.course.domain.dtos.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class SubscriptionUserIdRequestDTO {

    @NotNull
    private UUID userId;
}
