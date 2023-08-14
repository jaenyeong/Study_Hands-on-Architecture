package com.clean.architecture_clean.account.application.port.`in`

import com.clean.architecture_clean.account.domain.AccountId
import com.clean.architecture_clean.account.domain.Money

data class SendMoneyCommand(
    val sourceAccountId: AccountId,
    val targetAccountId: AccountId,
    val money: Money
) {
    init {
        require(money.isPositive) {
            "Money must be greater than or equal to zero"
        }
    }
}
