package com.clean.architecture_clean.account.domain

import java.time.LocalDateTime

class ActivityWindow(
    activities: List<Activity> = mutableListOf()
) {
    private val _activities: MutableList<Activity> = activities.toMutableList()

    val activities: List<Activity>
        get() = _activities.toList()

    fun startTimeStampOrThrow(): LocalDateTime {
        return _activities.minByOrNull { it.timeStamp }?.timeStamp
            ?: throw IllegalStateException("Activities list is empty")
    }

    fun endTimeStampOrThrow(): LocalDateTime {
        return _activities.maxByOrNull { it.timeStamp }?.timeStamp
            ?: throw IllegalStateException("Activities list is empty")
    }

    fun calculateBalance(accountId: AccountId): Money {
        val depositBalance: Money = _activities.sumMatchingBy { it.targetAccountId == accountId }
        val withdrawalBalance: Money = _activities.sumMatchingBy { it.sourceAccountId == accountId }

        return depositBalance - withdrawalBalance
    }

    private fun MutableList<Activity>.sumMatchingBy(predicate: (Activity) -> Boolean): Money {
        return this.filter(predicate)
            .map { it.money }
            .fold(Money.zero) { total, next -> total + next }
    }

    fun addActivity(activity: Activity) {
        _activities.add(activity)
    }
}
