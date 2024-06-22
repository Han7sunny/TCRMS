package com.kutca.tcrms.participantapplication.controller.dto.response;

import com.kutca.tcrms.universityapplication.controller.dto.response.UniversityApplicationResponseDto;
import lombok.Builder;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

public class ParticipantApplicationResponseDto {

    @SuperBuilder
    public static class FirstPeriod {

        private String eventName;

        private int participantCount;

        private int participantFee;

        public static FirstPeriod fromUniversityApplication(UniversityApplicationResponseDto.FirstPeriod universityApplication){
            return FirstPeriod.builder()
                    .eventName(universityApplication.getEventName())
                    .participantCount(universityApplication.getParticipantCount())
                    .participantFee(universityApplication.getParticipantFee())
                    .build();
        }

    }

    @Setter
    @SuperBuilder
    public static class SecondPeriod extends FirstPeriod {

        private int cancelParticipantCount;

        private int refundParticipantFee;

        public static SecondPeriod fromUniversityApplication(UniversityApplicationResponseDto.FirstPeriod universityApplication){
            return SecondPeriod.builder()
                    .eventName(universityApplication.getEventName())
                    .participantCount(universityApplication.getParticipantCount())
                    .participantFee(universityApplication.getParticipantFee())
                    .build();
        }

    }
}
