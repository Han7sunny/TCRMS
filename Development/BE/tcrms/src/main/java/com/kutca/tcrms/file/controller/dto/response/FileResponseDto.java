package com.kutca.tcrms.file.controller.dto.response;

import com.kutca.tcrms.file.entity.File;
import lombok.Builder;

@Builder
public class FileResponseDto {

    private Long fileId;

    private String fileName;

    public static FileResponseDto fromEntity(File file){
        return FileResponseDto.builder()
                .fileId(file.getFileId())
                .fileName(file.getFileName())
                .build();
    }

}
