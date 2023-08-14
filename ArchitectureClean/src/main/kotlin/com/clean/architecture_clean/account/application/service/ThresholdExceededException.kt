package com.clean.architecture_clean.account.application.service

import com.clean.architecture_clean.account.domain.Money

class ThresholdExceededException(threshold: Money, actual: Money)
    : RuntimeException("Maximum threshold for transferring money exceeded: tried to transfer $actual but threshold is ${threshold}!")
