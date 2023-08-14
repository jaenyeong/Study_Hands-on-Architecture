package com.clean.architecture_clean.account.domain

import java.time.LocalDateTime

class Activity(
    val ownerAccountId: AccountId,
    val sourceAccountId: AccountId,
    val targetAccountId: AccountId,
    val timeStamp: LocalDateTime,
    val money: Money,
    val id: ActivityId? = null
)
