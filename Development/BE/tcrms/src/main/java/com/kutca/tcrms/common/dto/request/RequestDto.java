package com.kutca.tcrms.common.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class RequestDto<T> {
    private Long userId;
    private List<T> requestDtoList;
}
