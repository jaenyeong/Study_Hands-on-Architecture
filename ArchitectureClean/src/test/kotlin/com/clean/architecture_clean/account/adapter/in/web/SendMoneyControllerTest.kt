package com.clean.architecture_clean.account.adapter.`in`.web

import com.clean.architecture_clean.account.application.port.`in`.SendMoneyCommand
import com.clean.architecture_clean.account.application.port.`in`.SendMoneyUseCase
import com.clean.architecture_clean.account.domain.AccountId
import com.clean.architecture_clean.account.domain.Money
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [SendMoneyController::class])
class SendMoneyControllerTest
@Autowired constructor(
    private val mockMvc: MockMvc,
    @MockBean
    private val sendMoneyUseCase: SendMoneyUseCase
) {
    @Test
    fun testSendMoney() {
        mockMvc.perform(
            post("/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}", 41L, 42L, 500)
                .header("Content-Type", "application/json"))
            .andExpect(status().isOk)

        verify(sendMoneyUseCase).sendMoney(
            SendMoneyCommand(
                sourceAccountId = AccountId(41L),
                targetAccountId = AccountId(42L),
                money = Money.of(500L)
            )
        )
    }
}
