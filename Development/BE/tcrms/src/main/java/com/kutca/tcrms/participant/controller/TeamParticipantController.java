package com.kutca.tcrms.participant.controller;

import com.kutca.tcrms.common.dto.request.RequestDto;
import com.kutca.tcrms.participant.controller.dto.request.TeamParticipantRequestDto;
import com.kutca.tcrms.participant.service.TeamParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TeamParticipantController {

    private final TeamParticipantService teamParticipantService;

    @PostMapping("/api/user/team")
    public ResponseEntity<?> registTeamList(@RequestBody RequestDto<TeamParticipantRequestDto.Regist> teamParticipantRequestDto){
        try {
            return new ResponseEntity<>(teamParticipantService.registTeamList(teamParticipantRequestDto), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
