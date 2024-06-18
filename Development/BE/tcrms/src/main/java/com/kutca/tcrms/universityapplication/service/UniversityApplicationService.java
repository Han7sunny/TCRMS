package com.kutca.tcrms.universityapplication.service;

import com.kutca.tcrms.common.dto.response.ResponseDto;
import com.kutca.tcrms.universityapplication.controller.dto.response.UniversityApplicationResponseDto;
import com.kutca.tcrms.universityapplication.repository.UniversityApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UniversityApplicationService {

    private final UniversityApplicationRepository universityApplicationRepository;

    public ResponseDto<?> isFinalSubmitConfirmed(Long userId){

        boolean isFinalSubmitted = universityApplicationRepository.existsByUser_UserId(userId);

        return ResponseDto.builder()
                .isSuccess(true)
                .payload(UniversityApplicationResponseDto.Status.builder()
                        .isFinalSubmitted(isFinalSubmitted)
                        .build())
                .build();
    }

}
