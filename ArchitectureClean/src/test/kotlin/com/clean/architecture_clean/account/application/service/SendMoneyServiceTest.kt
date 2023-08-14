package com.clean.architecture_clean.account.application.service

import com.clean.architecture_clean.account.application.port.`in`.SendMoneyCommand
import com.clean.architecture_clean.account.application.port.out.AccountLock
import com.clean.architecture_clean.account.application.port.out.LoadAccountPort
import com.clean.architecture_clean.account.application.port.out.UpdateAccountStatePort
import com.clean.architecture_clean.account.domain.Account
import com.clean.architecture_clean.account.domain.AccountId
import com.clean.architecture_clean.account.domain.Money
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify

class SendMoneyServiceTest : FunSpec() {
    private val loadAccountPort = mockk<LoadAccountPort>()
    private val accountLock = mockk<AccountLock>()
    private val updateAccountStatePort = mockk<UpdateAccountStatePort>()
    private val sendMoneyService = SendMoneyService(
        loadAccountPort = loadAccountPort,
        accountLock = accountLock,
        updateAccountStatePort = updateAccountStatePort,
        moneyTransferProperties = MoneyTransferProperties(Money.of(Long.MAX_VALUE))
    )
    private val capturedAccounts = mutableListOf<Account>()

    init {
        beforeTest {
            clearAllMocks()
        }

        test("given `Withdrawal Fails` then only source account is locked and released") {
            val sourceAccountId = AccountId(41L)
            val sourceAccount = givenAnAccountWithId(sourceAccountId)
            val targetAccountId = AccountId(42L)
            val targetAccount = givenAnAccountWithId(targetAccountId)
            val command = SendMoneyCommand(sourceAccountId, targetAccountId, Money.of(300L))

            givenWithdrawalWillFail(sourceAccount, command)
            givenDepositWillSucceed(targetAccount, command)

            sendMoneyService.sendMoney(command) shouldBe false

            verify { accountLock.lockAccount(sourceAccountId) }
            verify { accountLock.releaseAccount(sourceAccountId) }
            verify(exactly = 0) { accountLock.lockAccount(targetAccountId) }
        }

        test("transaction succeeds") {
            val sourceAccountId = AccountId(41L)
            val sourceAccount = givenAnAccountWithId(sourceAccountId)
            val targetAccountId = AccountId(42L)
            val targetAccount = givenAnAccountWithId(targetAccountId)
            val money = Money.of(500L)
            val command = SendMoneyCommand(sourceAccountId, targetAccountId, money)

            givenWithdrawalWillSucceed(sourceAccount, command)
            givenDepositWillSucceed(targetAccount, command)

            val success = sendMoneyService.sendMoney(command)

            success shouldBe true

            verify { accountLock.lockAccount(sourceAccountId) }
            verify { sourceAccount.withdraw(money, targetAccountId) }
            verify { accountLock.releaseAccount(sourceAccountId) }

            verify { accountLock.lockAccount(targetAccountId) }
            verify { targetAccount.deposit(money, sourceAccountId) }
            verify { accountLock.releaseAccount(targetAccountId) }

            thenAccountsHaveBeenUpdated(listOf(sourceAccountId, targetAccountId))
        }
    }

    private fun givenWithdrawalWillFail(account: Account, command: SendMoneyCommand) {
        every { account.withdraw(command.money, any()) } returns false
    }

    private fun givenDepositWillSucceed(account: Account, command: SendMoneyCommand) {
        every { account.deposit(command.money, any()) } returns true
    }

    private fun givenWithdrawalWillSucceed(account: Account, command: SendMoneyCommand) {
        every { account.withdraw(command.money, any()) } returns true
    }

    private fun givenAnAccountWithId(id: AccountId): Account {
        val account: Account = mockk<Account>()
        val accountSlot = slot<Account>()

        every { account.id } returns id
        every { loadAccountPort.loadAccount(id, any()) } returns account
        every { accountLock.lockAccount(id) } just Runs
        every { accountLock.releaseAccount(id) } just Runs
        every { updateAccountStatePort.updateActivities(capture(accountSlot)) } answers {
            capturedAccounts.add(accountSlot.captured)
        }

        return account
    }

    private fun thenAccountsHaveBeenUpdated(accountIds: List<AccountId>) {
        verify(exactly = accountIds.size) { updateAccountStatePort.updateActivities(any()) }

        val updatedAccountIds = capturedAccounts.map { it.id }

        updatedAccountIds shouldBe accountIds
    }
}
