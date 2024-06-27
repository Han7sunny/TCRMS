package com.kutca.tcrms.universityapplication.controller.dto.response;

import com.kutca.tcrms.universityapplication.entity.UniversityApplication;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

public class UniversityApplicationResponseDto {

    @Builder
    public static class Status {

        private boolean isFinalSubmitted;

    }

    @Getter
    @SuperBuilder
    public static class FirstPeriod {

        private String eventName;

        private int participantCount;

        private int participantFee;

        public static UniversityApplicationResponseDto.FirstPeriod fromEntity(UniversityApplication universityApplication){
            return UniversityApplicationResponseDto.FirstPeriod.builder()
                    .eventName(universityApplication.getEventName())
                    .participantCount(universityApplication.getTeamCount())
                    .participantFee(universityApplication.getTeamFee())
                    .build();
        }

    }

    @SuperBuilder
    public static class SecondPeriod extends FirstPeriod {

        private int cancelParticipantCount;

        private int refundParticipantFee;

    }

}
