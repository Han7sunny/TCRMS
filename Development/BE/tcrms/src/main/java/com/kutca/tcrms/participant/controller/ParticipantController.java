package com.kutca.tcrms.participant.controller;

import com.kutca.tcrms.common.dto.request.RequestDto;
import com.kutca.tcrms.participant.controller.dto.request.IndividualParticipantRequestDto;
import com.kutca.tcrms.participant.service.ParticipantService;
import com.kutca.tcrms.participantapplication.service.ParticipantApplicationService;
import io.jsonwebtoken.security.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ParticipantController {

    private final ParticipantService participantService;
    private final ParticipantApplicationService participantApplicationService;

    @GetMapping("/api/user/individual")
    public ResponseEntity<?> getIndividualList(@RequestParam Long userId) {
        try {
            return new ResponseEntity<>(participantService.getIndividualList(userId), HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>("개인전 신청 확인 예외발생", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/api/user/individual")
    public ResponseEntity<?> registIndividualList(@RequestBody RequestDto<IndividualParticipantRequestDto.Regist> individualParticipantRequestDto) {
        try {
            return new ResponseEntity<>(participantService.registIndividualList(individualParticipantRequestDto), HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>("개인전 신청 예외 발생", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/api/user/individual")
    public ResponseEntity<?> deleteIndividual(@RequestBody IndividualParticipantRequestDto.Delete individualParticipantRequestDto) {
        try {
            return new ResponseEntity<>(participantApplicationService.deleteParticipantApplication(individualParticipantRequestDto), HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>("개인전 신청 삭제 예외 발생", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
