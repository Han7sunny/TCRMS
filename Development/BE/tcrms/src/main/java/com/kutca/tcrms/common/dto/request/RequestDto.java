package com.kutca.tcrms.common.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RequestDto<T> {
    private Long userId;
    private List<T> requestDtoList;
}
