package com.kutca.tcrms.account.repository;

import com.kutca.tcrms.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
