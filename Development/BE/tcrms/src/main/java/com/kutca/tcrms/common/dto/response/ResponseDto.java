package com.kutca.tcrms.common.dto.response;

import lombok.Getter;
import lombok.Builder;

@Getter
@Builder
public class ResponseDto<T> {
    private Boolean isSuccess;
    private String message;
    private T payload;

//    public ResponseDto(Boolean isSuccess, String message) {
//        this.isSuccess = isSuccess;
//        this.message = message;
//        this.payload = null;
//    }
//
//    public ResponseDto(Boolean isSuccess, T payload) {
//        this.isSuccess = isSuccess;
//        this.message = null;
//        this.payload = payload;
//    }

}
