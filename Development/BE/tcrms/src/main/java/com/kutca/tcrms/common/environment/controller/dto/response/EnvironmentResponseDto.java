package com.kutca.tcrms.common.environment.controller.dto.response;

import lombok.Builder;

public class EnvironmentResponseDto {

    @Builder
    public static class Period{

        private String period;

    }

    @Builder
    public static class UserInfo{

        private Long userId;

        private boolean isFirstLogin;

        private String userAuth;

    }

}
