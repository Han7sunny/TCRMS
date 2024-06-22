package com.kutca.tcrms.common.environment.service;

import com.kutca.tcrms.common.dto.response.ResponseDto;
import com.kutca.tcrms.common.enums.DatePeriod;
import com.kutca.tcrms.common.environment.controller.dto.response.EnvironmentResponseDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class EnvironmentService {

    private final LocalDate firstPeriodStart = LocalDate.of(2024, 9,10);
    private final LocalDate firstPeriodEnd = LocalDate.of(2024, 9,25);
    private final LocalDate secondPeriodStart = LocalDate.of(2024, 10,10);
    private final LocalDate secondPeriodEnd = LocalDate.of(2024, 10,25);
    private final LocalDate competitionPeriodStart = LocalDate.of(2024, 11,1);
    private final LocalDate competitionPeriodEnd = LocalDate.of(2024, 11,5);

    public ResponseDto<?> getCurrentPeriod(){

        String currentPeriod = calculateCurrentPeriod();

        return ResponseDto.builder()
                .isSuccess(true)
                .payload(EnvironmentResponseDto.Period.builder().period(currentPeriod).build())
                .build();

    }

    public String calculateCurrentPeriod(){

        LocalDate today = LocalDate.now();

        if(isWithinPeriod(today, firstPeriodStart, firstPeriodEnd))
            return DatePeriod.FIRST.name();

        if(isWithinPeriod(today, secondPeriodStart, secondPeriodEnd))
            return DatePeriod.SECOND.name();

        if(isWithinPeriod(today, competitionPeriodStart, competitionPeriodEnd))
            return DatePeriod.COMPETITION.name();

        return DatePeriod.NONE.name();
    }

    private boolean isWithinPeriod(LocalDate date, LocalDate start, LocalDate end){
        return (date.isEqual(start) || date.isAfter(start)) && (date.isBefore(end) || date.isEqual(end));
    }

}
