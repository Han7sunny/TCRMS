package com.kutca.tcrms.user.controller.dto.response;

import com.kutca.tcrms.common.dto.response.ResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class LoginResponseDto extends ResponseDto {
    private Long userId;
    private String token;
    private Boolean isFirstLogin;
    private String auth;
}
