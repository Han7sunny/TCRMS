package com.kutca.tcrms.file.controller.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FilesRequestDto {

    List<FileRequestDto> fileInfos;

    public FilesRequestDto(List<FileRequestDto> fileInfos){
        this.fileInfos = fileInfos;
    }
}
