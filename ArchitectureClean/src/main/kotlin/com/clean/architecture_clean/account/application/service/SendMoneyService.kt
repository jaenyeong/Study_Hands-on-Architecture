package com.clean.architecture_clean.account.application.service

import com.clean.architecture_clean.account.application.port.`in`.SendMoneyCommand
import com.clean.architecture_clean.account.application.port.`in`.SendMoneyUseCase
import com.clean.architecture_clean.account.application.port.out.AccountLock
import com.clean.architecture_clean.account.application.port.out.LoadAccountPort
import com.clean.architecture_clean.account.application.port.out.UpdateAccountStatePort
import com.clean.architecture_clean.common.UseCase
import jakarta.transaction.Transactional
import java.time.LocalDateTime

@UseCase
@Transactional
class SendMoneyService(
    private val loadAccountPort: LoadAccountPort,
    private val accountLock: AccountLock,
    private val updateAccountStatePort: UpdateAccountStatePort,
    private val moneyTransferProperties: MoneyTransferProperties
) : SendMoneyUseCase {
    private val daysAgo: Long = 10L

    override fun sendMoney(command: SendMoneyCommand): Boolean {
        validateThreshold(command)

        val baselineDate = LocalDateTime.now().minusDays(daysAgo)!!
        val sourceAccount = loadAccountPort.loadAccount(command.sourceAccountId, baselineDate)
        val targetAccount = loadAccountPort.loadAccount(command.targetAccountId, baselineDate)

        accountLock.lockAccount(sourceAccount.id)
        if (!sourceAccount.withdraw(command.money, targetAccount.id)) {
            accountLock.releaseAccount(sourceAccount.id)
            return false
        }

        accountLock.lockAccount(targetAccount.id)
        if (!targetAccount.deposit(command.money, sourceAccount.id)) {
            accountLock.releaseAccount(sourceAccount.id)
            accountLock.releaseAccount(targetAccount.id)
            return false
        }

        updateAccountStatePort.updateActivities(sourceAccount)
        updateAccountStatePort.updateActivities(targetAccount)

        accountLock.releaseAccount(sourceAccount.id)
        accountLock.releaseAccount(targetAccount.id)

        return true
    }

    private fun validateThreshold(command: SendMoneyCommand) {
        val maxTransferThreshold = moneyTransferProperties.maxTransferThreshold

        if (command.money.isGreaterThan(maxTransferThreshold)) {
            throw ThresholdExceededException(maxTransferThreshold, command.money)
        }
    }
}
