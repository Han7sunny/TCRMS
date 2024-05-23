package com.kutca.tcrms.participant.controller.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;


public class IndividualParticipantRequestDto {

    @Getter
    @Builder
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
    public static class Modify {

        private Long userId;    //  useless

        private Boolean isParticipantChange;

        private Long participantId;

        private String name;

        private String gender;

        private Boolean isForeigner;

        private String nationality;

        private String identityNumber;

        private String phoneNumber;

        private Boolean isEventChange;

        private Map<Long, Long> participantApplicationInfos;

        private Boolean isWeightClassChange;

        private Long weightClassId;
    }

    @Getter
    @Builder
    public static class Delete {

        private Long userId;

        private Long participantId;

        private List<Long> participantApplicationIds;
    }

}
