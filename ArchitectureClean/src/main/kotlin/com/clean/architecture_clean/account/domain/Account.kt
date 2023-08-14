package com.clean.architecture_clean.account.domain

import java.time.LocalDateTime

class Account(
    val id: AccountId,
    val baselineBalance: Money,
    val activityWindow: ActivityWindow
) {
    val currentBalance: Money
        get() = baselineBalance + activityWindow.calculateBalance(id)

    fun withdraw(money: Money, targetAccountId: AccountId): Boolean {
        if (canNotWithDraw(money)) {
            return false
        }

        activityWindow.addActivity(
            Activity(
                ownerAccountId = id,
                sourceAccountId = id,
                targetAccountId = targetAccountId,
                timeStamp = LocalDateTime.now(),
                money = money
            )
        )

        return true
    }

    private fun canNotWithDraw(money: Money): Boolean {
        return (currentBalance - money).isNegative
    }

    fun deposit(money: Money, sourceAccountId: AccountId): Boolean {
        activityWindow.addActivity(
            Activity(
                ownerAccountId = id,
                sourceAccountId = sourceAccountId,
                targetAccountId = id,
                timeStamp = LocalDateTime.now(),
                money = money
            )
        )

        return true
    }
}
