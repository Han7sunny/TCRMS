package com.kutca.tcrms.account.service;

import com.kutca.tcrms.account.controller.dto.response.AccountResponseDto;
import com.kutca.tcrms.account.entity.Account;
import com.kutca.tcrms.common.dto.response.ResponseDto;
import com.kutca.tcrms.secondperiod.entity.SecondPeriod;
import com.kutca.tcrms.secondperiod.repository.SecondPeriodRepository;
import com.kutca.tcrms.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final SecondPeriodRepository secondPeriodRepository;

    @Transactional(readOnly = true)
    public ResponseDto<?> getDepositAccountInfo(){

        //  관리자 userId
        Long userId = null;
        Optional<SecondPeriod> findSecondPeriod = secondPeriodRepository.findByUser_UserId(userId);
        if(findSecondPeriod.isEmpty()){
            return ResponseDto.builder()
                    .isSuccess(false)
                    .message("입금 계좌 정보를 조회할 수 없습니다.")
                    .build();
        }

        Account depositAccount = findSecondPeriod.get().getAccount();

        return ResponseDto.builder()
                .isSuccess(true)
                .payload(AccountResponseDto.builder()
                        .accountBank(depositAccount.getAccountBank())
                        .accountNumber(depositAccount.getAccountNumber())
                        .depositOwnerName(depositAccount.getDepositOwnerName())
                        .build())
                .build();
    }
}
