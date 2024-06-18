package com.kutca.tcrms.universityapplication.controller.dto.response;

import lombok.Builder;

public class UniversityApplicationResponseDto {

    @Builder
    public static class Status {

        private boolean isFinalSubmitted;

    }
}
