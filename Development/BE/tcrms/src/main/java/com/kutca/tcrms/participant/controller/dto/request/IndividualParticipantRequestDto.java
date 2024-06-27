package com.kutca.tcrms.participant.controller.dto.request;

import com.kutca.tcrms.file.controller.dto.request.FilesRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;


public class IndividualParticipantRequestDto {

    @Getter
    @Builder
    @Schema(name = "IndividualRegist")
    public static class Regist {

        private String name;

        private String gender;

        private Boolean isForeigner;

        private String nationality;

        private String identityNumber;

        private String phoneNumber;

        private List<Long> eventIds;

        private Long weightClassId;
    }

    @Getter
    @Builder
    @Schema(name = "IndividualModify")
    public static class Modify {

        private Long participantId;

        private Boolean isParticipantChange;

        private String name;

        private String gender;

        private Boolean isForeigner;

        private String nationality;

        private String identityNumber;

        private String phoneNumber;

        private Boolean isEventChange;

        private Map<Long, Long> eventInfo;

        private Boolean isWeightClassChange;

        private Long weightClassId;
    }

    @Getter
    @Builder
    @Schema(name = "IndividualDelete")
    public static class Delete {

        private Long userId;

        private Long participantId;

        private List<Long> participantApplicationIds;
    }

}
