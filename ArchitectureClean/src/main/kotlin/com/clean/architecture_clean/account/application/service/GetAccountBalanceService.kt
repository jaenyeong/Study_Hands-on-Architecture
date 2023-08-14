package com.clean.architecture_clean.account.application.service

import com.clean.architecture_clean.account.application.port.`in`.GetAccountBalanceQuery
import com.clean.architecture_clean.account.application.port.out.LoadAccountPort
import com.clean.architecture_clean.account.domain.AccountId
import com.clean.architecture_clean.account.domain.Money
import java.time.LocalDateTime

class GetAccountBalanceService(
    private val loadAccountPort: LoadAccountPort
) : GetAccountBalanceQuery {
    override fun getAccountBalance(accountId: AccountId): Money {
        return loadAccountPort.loadAccount(accountId, LocalDateTime.now())
            .currentBalance
    }
}
