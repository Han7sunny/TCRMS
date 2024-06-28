package com.kutca.tcrms.common.environment.controller.dto.response;

import lombok.Builder;
import lombok.Getter;

public class EnvironmentResponseDto {

    @Getter
    @Builder
    public static class Period{

        private String period;

    }

    @Getter
    @Builder
    public static class UserInfo{

        private Long userId;

        private boolean isFirstLogin;

        private String userAuth;

    }

}
