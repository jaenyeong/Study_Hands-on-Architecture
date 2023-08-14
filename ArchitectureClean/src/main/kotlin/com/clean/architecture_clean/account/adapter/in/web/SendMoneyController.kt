package com.clean.architecture_clean.account.adapter.`in`.web

import com.clean.architecture_clean.account.application.port.`in`.SendMoneyCommand
import com.clean.architecture_clean.account.application.port.`in`.SendMoneyUseCase
import com.clean.architecture_clean.account.domain.AccountId
import com.clean.architecture_clean.account.domain.Money
import com.clean.architecture_clean.common.WebAdapter
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@WebAdapter
@RestController
class SendMoneyController(
    private val sendMoneyUseCase: SendMoneyUseCase
) {
    @PostMapping(path = ["/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}"])
    fun sendMoney(
        @PathVariable("sourceAccountId") sourceAccountId: Long,
        @PathVariable("targetAccountId") targetAccountId: Long,
        @PathVariable("amount") amount: Long
    ) {
        val command = SendMoneyCommand(
            sourceAccountId = AccountId(sourceAccountId),
            targetAccountId = AccountId(targetAccountId),
            money = Money.of(amount)
        )

        sendMoneyUseCase.sendMoney(command)
    }
}
