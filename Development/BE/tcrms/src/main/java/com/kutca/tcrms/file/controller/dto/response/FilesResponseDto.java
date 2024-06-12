package com.kutca.tcrms.file.controller.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FilesResponseDto {

    List<FileResponseDto.Info> fileInfos;

    public FilesResponseDto(List<FileResponseDto.Info> fileInfos){
        this.fileInfos = fileInfos;
    }
}
