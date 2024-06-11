package com.kutca.tcrms.participant.controller.dto.response;

import com.kutca.tcrms.file.controller.dto.response.FileResponseDto;
import com.kutca.tcrms.file.controller.dto.response.FilesResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ParticipantFileResponseDto {

    private Long participantId;

    private String name;

    private Boolean isForeigner;

    private String identityNumber;

    private List<String> types;

    private List<String> events;

    private List<FileResponseDto> fileInfos;    //  FilesResponseDto?

    private Boolean isAllFileConfirmed;
}
