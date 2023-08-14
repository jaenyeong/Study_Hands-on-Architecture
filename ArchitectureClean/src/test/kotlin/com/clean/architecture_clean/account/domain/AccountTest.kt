package com.clean.architecture_clean.account.domain

import com.clean.architecture_clean.common.mockAccountData
import com.clean.architecture_clean.common.mockActivityData
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class AccountTest : FunSpec({
    test("`calculateBalance` should return correct total based on baseline and activities") {
        val accountId = 1L

        val account = mockAccountData(
            accountId = accountId,
            baselineBalance = Money.of(555L),
            activities = listOf(
                mockActivityData(targetAccountId = accountId, money = Money.of(999L)),
                mockActivityData(targetAccountId = accountId, money = Money.of(1L)),
            )
        )

        account.currentBalance shouldBe Money.of(1_555L)
    }

    test("`Withdraw` with sufficient balance should succeed") {
        val accountId = 1L

        val account = mockAccountData(
            accountId = accountId,
            baselineBalance = Money.of(555L),
            activities = listOf(
                mockActivityData(targetAccountId = accountId, money = Money.of(999L)),
                mockActivityData(targetAccountId = accountId, money = Money.of(1L)),
            )
        )

        val isWithdrawalResult = account.withdraw(
            money = Money.of(555L),
            targetAccountId = AccountId(99L)
        )

        isWithdrawalResult shouldBe true
        account.activityWindow.activities.size shouldBe 3
        account.currentBalance shouldBe Money.of(1_000L)
    }

    test("`Withdraw` with insufficient balance should fail") {
        val accountId = 1L

        val account = mockAccountData(
            accountId = accountId,
            baselineBalance = Money.of(555L),
            activities = listOf(
                mockActivityData(targetAccountId = accountId, money = Money.of(999L)),
                mockActivityData(targetAccountId = accountId, money = Money.of(1L)),
            )
        )

        val isWithdrawalResult = account.withdraw(
            money = Money.of(1_556L),
            targetAccountId = AccountId(99L)
        )

        isWithdrawalResult shouldBe false
        account.activityWindow.activities.size shouldBe 2
        account.currentBalance shouldBe Money.of(1_555L)
    }

    test("`Deposit` should always succeed and adjust balance") {
        val accountId = 1L

        val account = mockAccountData(
            accountId = accountId,
            baselineBalance = Money.of(555L),
            activities = listOf(
                mockActivityData(targetAccountId = accountId, money = Money.of(999L)),
                mockActivityData(targetAccountId = accountId, money = Money.of(1L)),
            )
        )

        val isDepositResult = account.deposit(
            money = Money.of(445L),
            sourceAccountId = AccountId(99L)
        )

        isDepositResult shouldBe true
        account.activityWindow.activities.size shouldBe 3
        account.currentBalance shouldBe Money.of(2_000L)
    }
})
