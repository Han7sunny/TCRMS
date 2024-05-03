package com.kutca.tcrms.common.dto.response;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ResponseDto {
    private Boolean isSuccess;
    private String message;
}
