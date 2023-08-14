package com.clean.architecture_clean

import com.clean.architecture_clean.account.application.port.out.LoadAccountPort
import com.clean.architecture_clean.account.domain.Account
import com.clean.architecture_clean.account.domain.AccountId
import com.clean.architecture_clean.account.domain.Money
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.jdbc.Sql
import java.time.LocalDateTime

@SpringBootTest(webEnvironment = RANDOM_PORT)
class SendMoneySystemTest
@Autowired constructor(
    private val restTemplate: TestRestTemplate,
    private val loadAccountPort: LoadAccountPort
) {
    @Sql("SendMoneySystemTest.sql")
    @Test
    fun sendMoney() {
        val sourceAccount = loadSourceAccount()
        val targetAccount = loadTargetAccount()
        val transferredAmount = Money.of(500L)

        val initialSourceBalance = sourceAccount.currentBalance
        val initialTargetBalance = targetAccount.currentBalance

        val response = whenSendMoney(
            sourceAccount.id,
            targetAccount.id,
            transferredAmount
        )

        response.statusCode shouldBe HttpStatus.OK
        loadSourceAccount().currentBalance shouldBe (initialSourceBalance - transferredAmount)
        loadTargetAccount().currentBalance shouldBe (initialTargetBalance + transferredAmount)
    }

    private fun loadSourceAccount(): Account {
        return loadAccount(AccountId(1L))
    }

    private fun loadTargetAccount(): Account {
        return loadAccount(AccountId(2L))
    }

    private fun loadAccount(accountId: AccountId): Account {
        return loadAccountPort.loadAccount(
            accountId = accountId,
            baselineDate = LocalDateTime.now()
        )
    }

    private fun whenSendMoney(sourceAccountId: AccountId, targetAccountId: AccountId, amount: Money): ResponseEntity<Any> {
        val headers = HttpHeaders().apply {
            add("Content-Type", "application/json")
        }
        val request = HttpEntity<Void>(null, headers)

        return restTemplate.exchange(
            "/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}",
            HttpMethod.POST,
            request,
            Any::class.java,
            sourceAccountId.value,
            targetAccountId.value,
            amount.amount
        )
    }
}
