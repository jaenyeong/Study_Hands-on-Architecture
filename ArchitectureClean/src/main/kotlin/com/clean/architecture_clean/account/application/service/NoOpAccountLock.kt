package com.clean.architecture_clean.account.application.service

import com.clean.architecture_clean.account.application.port.out.AccountLock
import com.clean.architecture_clean.account.domain.AccountId
import org.springframework.stereotype.Component

@Component
class NoOpAccountLock : AccountLock {
    override fun lockAccount(accountId: AccountId) {
        // lock logic
    }

    override fun releaseAccount(accountId: AccountId) {
        // release logic
    }
}
