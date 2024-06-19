package com.kutca.tcrms.user.controller.dto.request;

import lombok.Getter;

public class UserRequestDto {

    @Getter
    public static class Info {

        private Long userId;

        private String phoneNumber;

        private String depositorName;

    }
}
