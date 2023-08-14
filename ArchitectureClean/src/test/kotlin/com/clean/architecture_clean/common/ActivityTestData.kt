package com.clean.architecture_clean.common

import com.clean.architecture_clean.account.domain.AccountId
import com.clean.architecture_clean.account.domain.Activity
import com.clean.architecture_clean.account.domain.Money
import java.time.LocalDateTime

fun mockActivityData(
    ownerAccountId: Long = 42L,
    sourceAccountId: Long = 42L,
    targetAccountId: Long = 40L,
    timestamp: LocalDateTime = LocalDateTime.now(),
    money: Money = Money.of(999L)
): Activity {
    return Activity(
        ownerAccountId = AccountId(ownerAccountId),
        sourceAccountId = AccountId(sourceAccountId),
        targetAccountId = AccountId(targetAccountId),
        timeStamp = timestamp,
        money = money,
    )
}
