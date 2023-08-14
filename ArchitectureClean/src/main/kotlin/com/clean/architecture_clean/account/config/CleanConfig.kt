package com.clean.architecture_clean.account.config

import com.clean.architecture_clean.account.application.service.MoneyTransferProperties
import com.clean.architecture_clean.account.domain.Money
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(CleanConfigProperties::class)
class CleanConfig {
    @Bean
    fun moneyTransferProperties(cleanConfigProperties: CleanConfigProperties): MoneyTransferProperties {
        return MoneyTransferProperties(Money.of(cleanConfigProperties.transferThreshold))
    }
}
