package com.kutca.tcrms.account.controller.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccountResponseDto {

    private String accountBank;

    private String accountNumber;

    private String depositOwnerName;
}
