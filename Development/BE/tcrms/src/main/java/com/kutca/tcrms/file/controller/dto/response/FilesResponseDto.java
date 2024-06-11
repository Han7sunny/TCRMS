package com.kutca.tcrms.file.controller.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public class FilesResponseDto {

    List<FileResponseDto> fileInfos;

    public FilesResponseDto(List<FileResponseDto> fileInfos){
        this.fileInfos = fileInfos;
    }
}
