package com.kutca.tcrms.participantapplication.service;

import com.kutca.tcrms.common.dto.response.ResponseDto;
import com.kutca.tcrms.participantapplication.repository.ParticipantApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParticipantApplicationService {

    private final ParticipantApplicationRepository participantApplicationRepository;

    public ResponseDto<?> deleteParticipantApplication(){
        //  1. participantApplication (참가자 신청 종목) 삭제
        //  2. participantApplication에서 participant 하나도 없으면 parcitipant 삭제
        return ResponseDto.builder()
                .isSuccess(true)
                .build();
    }
}
