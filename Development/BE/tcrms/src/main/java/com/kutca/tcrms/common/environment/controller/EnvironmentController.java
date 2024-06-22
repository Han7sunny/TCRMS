package com.kutca.tcrms.common.environment.controller;

import com.kutca.tcrms.common.dto.response.ResponseDto;
import com.kutca.tcrms.common.environment.service.EnvironmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@Tag(name = "EnvironmentController")
public class EnvironmentController {

    private final EnvironmentService environmentService;

    @Operation(summary = "현재 신청기간 정보 조회")
    @ApiResponse(content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    @GetMapping("/api/env/period")
    public ResponseEntity<?> getCurrentPeriod(){
        try {
            return new ResponseEntity<>(environmentService.getCurrentPeriod(), HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>("예외발생", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
