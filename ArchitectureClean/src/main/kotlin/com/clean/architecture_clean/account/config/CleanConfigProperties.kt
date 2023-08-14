package com.clean.architecture_clean.account.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "clean")
class CleanConfigProperties(
    val transferThreshold: Long = Long.MAX_VALUE
)
