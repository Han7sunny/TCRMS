package com.kutca.tcrms.file.controller.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FileRequestDto {

    private Long fileId;    //  nullable

    private String fileName;

}
