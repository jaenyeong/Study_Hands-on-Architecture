package com.clean.architecture_clean.account.application.service

import com.clean.architecture_clean.account.domain.Money

class MoneyTransferProperties(
    val maxTransferThreshold: Money = Money.of(1_000_000L)
)
