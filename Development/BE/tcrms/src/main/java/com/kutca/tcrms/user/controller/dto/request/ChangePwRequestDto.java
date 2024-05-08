package com.kutca.tcrms.user.controller.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChangePwRequestDto {
    private Long userId;
    private String initPassword;
    private String newPassword;
}
