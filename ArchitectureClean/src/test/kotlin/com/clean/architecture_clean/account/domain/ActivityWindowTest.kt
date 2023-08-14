package com.clean.architecture_clean.account.domain

import com.clean.architecture_clean.common.mockActivityData
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class ActivityWindowTest : FunSpec() {
    private val startDate: LocalDateTime = LocalDateTime.of(2023, 8, 3, 0, 0)
    private val inBetweenDate: LocalDateTime = LocalDateTime.of(2023, 8, 10, 0, 0)
    private val endDate: LocalDateTime = LocalDateTime.of(2023, 8, 15, 0, 0)

    init {
        test("`startTimestamp` matches `startDate`") {
            val activityWindow = ActivityWindow(
                listOf(
                    mockActivityData(timestamp = startDate),
                    mockActivityData(timestamp = inBetweenDate),
                    mockActivityData(timestamp = endDate),
                )
            )

            activityWindow.startTimeStampOrThrow() shouldBe startDate
        }

        test("`endTimestamp` matches `endDate`") {
            val activityWindow = ActivityWindow(
                listOf(
                    mockActivityData(timestamp = startDate),
                    mockActivityData(timestamp = inBetweenDate),
                    mockActivityData(timestamp = endDate),
                )
            )

            activityWindow.endTimeStampOrThrow() shouldBe endDate
        }

        test("`calculateBalance` reflects transaction amounts") {
            val firstAccountId = 1L
            val secondAccountId = 2L

            val activityWindow = ActivityWindow(
                activities = listOf(
                    mockActivityData(
                        sourceAccountId = firstAccountId,
                        targetAccountId = secondAccountId,
                        money = Money.of(999L)
                    ),
                    mockActivityData(
                        sourceAccountId = firstAccountId,
                        targetAccountId = secondAccountId,
                        money = Money.of(1L)
                    ),
                    mockActivityData(
                        sourceAccountId = secondAccountId,
                        targetAccountId = firstAccountId,
                        money = Money.of(500L)
                    ),
                )
            )

            activityWindow.calculateBalance(AccountId(firstAccountId)) shouldBe Money.of(-500)
            activityWindow.calculateBalance(AccountId(secondAccountId)) shouldBe Money.of(500)
        }
    }
}
