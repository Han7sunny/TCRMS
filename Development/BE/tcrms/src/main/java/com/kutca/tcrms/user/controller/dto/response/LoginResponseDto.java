package com.kutca.tcrms.user.controller.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {
    private Long userId;
    private String token;
    private Boolean isFirstLogin;
    private String auth;

}
