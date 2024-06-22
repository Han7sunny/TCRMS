package com.kutca.tcrms.universityapplication.controller.dto.response;

import com.kutca.tcrms.universityapplication.entity.UniversityApplication;
import lombok.Builder;
import lombok.Getter;

public class UniversityApplicationResponseDto {

    @Builder
    public static class Status {

        private boolean isFinalSubmitted;

    }

    @Getter
    @Builder
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

    @Builder
    public static class SecondPeriod extends FirstPeriod {

        private int cancelParticipantCount;

        private int refundParticipantFee;

    }

}
