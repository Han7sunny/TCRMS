package com.kutca.tcrms.participant.controller;

import com.kutca.tcrms.participant.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ParticipantController {

    private final ParticipantService participantService;

    @GetMapping("/api/user/individual-list")
    public ResponseEntity<?> getIndividualList(@RequestParam Long userId) {
        try {
            return new ResponseEntity<>(participantService.getIndividualList(userId), HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>("getIndividualList 예외발생", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
