package com.kutca.tcrms.participant.service;

import com.kutca.tcrms.common.dto.response.ResponseDto;
import com.kutca.tcrms.participant.controller.dto.response.IndividualParticipantResponseDto;
import com.kutca.tcrms.participant.controller.dto.response.ParticipantResponseDto;
import com.kutca.tcrms.participant.controller.dto.response.ParticipantsResponseDto;
import com.kutca.tcrms.participant.entity.Participant;
import com.kutca.tcrms.participant.repository.ParticipantRepository;
import com.kutca.tcrms.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;

    public ResponseDto<?> getIndividualList(Long userId) {

        List<Participant> findParticipantList = participantRepository.findAllByUserId(userId);

        if(findParticipantList.isEmpty()){
            return ResponseDto.builder()
                    .isSuccess(true)
                    .payload(
                            ParticipantResponseDto.builder()
//                                    .isEditable()
//                                    .isDepositConfirmed()
                                    .isParticipantExists(false)
                                    .build()

                    )
                    .build();
        }

        ParticipantsResponseDto<IndividualParticipantResponseDto> participantsResponseDto = new ParticipantsResponseDto();

        return ResponseDto.builder()
                .isSuccess(true)
                .payload(
                        ParticipantResponseDto.builder()
//                                .isEditable()
//                                .isDepositConfirmed()
                                .isParticipantExists(true)
                                .participants(participantsResponseDto)
                                .build()
                )
                .build();
    }
}
