package com.clean.architecture_clean.account.domain

import java.math.BigInteger

@JvmInline
value class Money(
    val amount: BigInteger,
) {
    val isPositive: Boolean
        get() = amount > BigInteger.ZERO

    val isNegative: Boolean
        get() = amount < BigInteger.ZERO

    fun longValueOrThrow(): Long = amount.longValueExact()

    fun isGreaterThanOrEqualTo(otherMoney: Money): Boolean = amount >= otherMoney.amount

    fun isGreaterThan(otherMoney: Money): Boolean = (amount > otherMoney.amount)

    operator fun plus(otherMoney: Money): Money = Money(amount + otherMoney.amount)

    operator fun minus(otherMoney: Money): Money = Money(amount - otherMoney.amount)

    companion object {
        val zero: Money = Money(BigInteger.ZERO)

        fun of(amount: Long): Money {
            return Money(amount = BigInteger.valueOf(amount))
        }
    }
}
