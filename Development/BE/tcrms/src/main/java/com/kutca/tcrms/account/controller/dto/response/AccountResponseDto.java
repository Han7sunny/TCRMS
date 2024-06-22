package com.kutca.tcrms.account.controller.dto.response;

import lombok.Builder;

@Builder
public class AccountResponseDto {

    private String accountBank;

    private String accountNumber;

    private String depositOwnerName;
}
