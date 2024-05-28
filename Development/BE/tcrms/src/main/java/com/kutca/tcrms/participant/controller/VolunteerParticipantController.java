package com.kutca.tcrms.participant.controller;

import com.kutca.tcrms.common.dto.request.RequestDto;
import com.kutca.tcrms.common.dto.response.ResponseDto;
import com.kutca.tcrms.participant.controller.dto.request.VolunteerParticipantRequestDto;
import com.kutca.tcrms.participant.service.VolunteerParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class VolunteerParticipantController {

    private final VolunteerParticipantService volunteerParticipantService;

    @GetMapping("/api/user/volunteer")
    public ResponseEntity<?> getVolunteerList(@RequestParam Long userId) {
        try {
            return new ResponseEntity<>(volunteerParticipantService.getVolunteerList(userId), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/api/user/volunteer")
    public ResponseEntity<?> registVolunteerList(@RequestBody RequestDto<VolunteerParticipantRequestDto.Regist> volunteerParticipantRequestDto) {
        try {
            return new ResponseEntity<>(volunteerParticipantService.registVolunteer(volunteerParticipantRequestDto), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/api/user/volunteer")
    public ResponseEntity<?> modifyVolunteer(@RequestBody VolunteerParticipantRequestDto.Modify volunteerParticipantRequestDto) {
        try {
            return new ResponseEntity<>(volunteerParticipantService.modifyVolunteer(volunteerParticipantRequestDto), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/api/user/volunteer")
    public ResponseEntity<?> deleteVolunteer(@RequestBody VolunteerParticipantRequestDto.Delete volunteerParticipantRequestDto) {
        try {
            return new ResponseEntity<>(volunteerParticipantService.deleteVolunteer(volunteerParticipantRequestDto), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
