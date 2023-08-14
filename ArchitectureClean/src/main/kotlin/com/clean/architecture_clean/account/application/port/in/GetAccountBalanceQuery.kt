package com.clean.architecture_clean.account.application.port.`in`

import com.clean.architecture_clean.account.domain.AccountId

import com.clean.architecture_clean.account.domain.Money

interface GetAccountBalanceQuery {
    fun getAccountBalance(accountId: AccountId): Money
}
