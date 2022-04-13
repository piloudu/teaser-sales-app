package com.example.sample_sales_app.view_models

import com.example.sample_sales_app.data.CurrencyChange
import com.example.sample_sales_app.data.Order
import com.example.sample_sales_app.view_models.LoginViewModel.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.*

class LoginTests {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var result: RestResult
    private val dispatcher = TestCoroutineDispatcher()
    private val mapper = jacksonObjectMapper()

    private fun withScopeInitializer(scope: suspend CoroutineScope.() -> Unit) {
        runBlocking {
            Dispatchers.setMain(dispatcher)
            loginViewModel = LoginViewModel()
            scope()
        }
    }

    @DisplayName("Should retrieve currency JSON")
    @Test
    fun `is REST call result for currencyRequest a success`() {
        withScopeInitializer {
            result = loginViewModel.performRestCall(loginViewModel.currencyRequest)
            println(result.message)
            result.status shouldBe RestStatus.SUCCESS
        }
    }

    @DisplayName("Should retrieve ordrs JSON")
    @Test
    fun `is REST call result for ordersRequest a success`() {
        withScopeInitializer {
            result = loginViewModel.performRestCall(loginViewModel.ordersRequest)
            result.status shouldBe RestStatus.SUCCESS
        }
    }

    @DisplayName("On JSON retrieve")
    @Nested
    inner class DeserializeCurrencyChange {
        private val mockCurrencyChange = """{"from": "CA", "to": "EUR", "rate": "0.76"}"""

        @DisplayName("should deserialize mock individual element")
        @BeforeAll
        fun init() {
            withScopeInitializer {
                result = loginViewModel.performRestCall(loginViewModel.currencyRequest)
            }
        }

        @DisplayName("should deserialize the currency change list")
        @Test
        fun `is currency sample deserialized properly`() {
            val currencyChange: CurrencyChange = mapper.readValue(mockCurrencyChange)
            currencyChange shouldBe CurrencyChange("CAD", "EUR", "0.76")
        }

        @Test
        fun `is currency JSON deserializer properly`() {
            val currencyChanges: List<CurrencyChange> = mapper.readValue(result.message)
            currencyChanges[1] shouldBe CurrencyChange("AUD", "USD", "0.77")
        }
    }

    @Nested
    inner class DeserializeOrder {
        private val mockOrder = """{"sku": "T2006", "amount": "10.00", "currency": "USD"}"""

        @Test
        fun `is orders JSON deserialized properly`() {
            val order: Order = mapper.readValue(mockOrder)
            order shouldBe Order("T2006", "10.00", "USD")
        }
    }
}