package com.clean.architecture_clean.account.domain

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class MoneyTest : FunSpec({
    data class TestData(
        val firstAmount: Long,
        val secondAmount: Long,
        val expectedIsGreaterThan: Boolean,
        val expectedIsGreaterThanOrEqualTo: Boolean
    )

    val testDataList = listOf(
        TestData(1L, 1L, expectedIsGreaterThan = false, expectedIsGreaterThanOrEqualTo = true),
        TestData(0L, 0L, expectedIsGreaterThan = false, expectedIsGreaterThanOrEqualTo = true),
        TestData(10L, 1L, expectedIsGreaterThan = true, expectedIsGreaterThanOrEqualTo = true),
        TestData(10_000_000L, 10_000_000L, expectedIsGreaterThan = false, expectedIsGreaterThanOrEqualTo = true),
        TestData(-1L, -1L, expectedIsGreaterThan = false, expectedIsGreaterThanOrEqualTo = true),
        TestData(-10L, -100L, expectedIsGreaterThan = true, expectedIsGreaterThanOrEqualTo = true),
        TestData(-100_000_000L, -100L, expectedIsGreaterThan = false, expectedIsGreaterThanOrEqualTo = false),
        TestData(200, -100L, expectedIsGreaterThan = true, expectedIsGreaterThanOrEqualTo = true),
    )

    testDataList.forEach { testData ->
        with(testData) {
            val firstMoney = Money.of(firstAmount)
            val secondMoney = Money.of(secondAmount)

            test("`isGreaterThan` ${firstAmount}/${secondAmount} should reflect $expectedIsGreaterThan") {
                firstMoney.isGreaterThan(secondMoney) shouldBe expectedIsGreaterThan
            }

            test("`isGreaterThanOrEqualTo` ${firstAmount}/${secondAmount} should return $expectedIsGreaterThanOrEqualTo") {
                firstMoney.isGreaterThanOrEqualTo(secondMoney) shouldBe expectedIsGreaterThanOrEqualTo
            }
        }
    }
})
