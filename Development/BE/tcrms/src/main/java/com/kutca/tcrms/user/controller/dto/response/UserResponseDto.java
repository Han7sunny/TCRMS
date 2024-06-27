package com.kutca.tcrms.user.controller.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {

    private String userName;

    private String phoneNumber;

    private String universityName;

    private String depositorName;

}
