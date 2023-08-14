package com.clean.architecture_clean.common

import com.clean.architecture_clean.account.domain.Account
import com.clean.architecture_clean.account.domain.AccountId
import com.clean.architecture_clean.account.domain.Activity
import com.clean.architecture_clean.account.domain.ActivityWindow
import com.clean.architecture_clean.account.domain.Money

fun mockAccountData(
    accountId: Long = 42L,
    baselineBalance: Money = Money.of(999L),
    activities: List<Activity> = listOf(mockActivityData(), mockActivityData())
): Account {
    return Account(
        id = AccountId(accountId),
        baselineBalance = baselineBalance,
        activityWindow = ActivityWindow(activities = activities)
    )
}
