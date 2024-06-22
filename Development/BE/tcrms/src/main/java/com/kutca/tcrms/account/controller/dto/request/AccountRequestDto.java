package com.kutca.tcrms.account.controller.dto.request;

import lombok.Getter;

@Getter
public class AccountRequestDto {

    private Long userId;

    private String accountBank;

    private String accountNumber;

    private String depositOwnerName;

}
