package com.kutca.tcrms.file.controller.dto.response;

import com.kutca.tcrms.file.entity.File;
import lombok.Builder;
import lombok.Getter;

public class FileResponseDto {

    @Getter
    @Builder
    public static class Info {

        private Long fileId;

        private String fileName;

        public static FileResponseDto.Info fromEntity(File file) {
            return FileResponseDto.Info.builder()
                    .fileId(file.getFileId())
                    .fileName(file.getFileName())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class Status {

        private boolean isFileCompleted;

    }
}
