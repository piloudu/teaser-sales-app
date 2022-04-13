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
import org.junit.Test


class LoginViewModelTest {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var result: RestResult
    private val dispatcher = TestCoroutineDispatcher()
    private val mockCurrencyChange = """{"from": "CAD", "to": "EUR", "rate": "0.76"}"""
    private val mockOrder = """{"sku": "T2006", "amount": "10.00", "currency": "USD"}"""
    private val mapper = jacksonObjectMapper()

    private fun withScopeInitializer(scope: suspend CoroutineScope.() -> Unit) {
        runBlocking {
            Dispatchers.setMain(dispatcher)
            loginViewModel = LoginViewModel()
            scope()
        }
    }

    @Test
    fun `is REST call result for currencyRequest a success`() {
        withScopeInitializer {
            result = loginViewModel.performRestCall(loginViewModel.currencyRequest)
            println(result.message)
            result.status shouldBe RestStatus.SUCCESS
        }
    }

    @Test
    fun `is REST call result for ordersRequest a success`() {
        withScopeInitializer {
            result = loginViewModel.performRestCall(loginViewModel.ordersRequest)
            result.status shouldBe RestStatus.SUCCESS
        }
    }

    @Test
    fun `is currency JSON deserialized properly`() {
        val currencyChange: CurrencyChange = mapper.readValue(mockCurrencyChange)
        currencyChange shouldBe CurrencyChange("CAD", "EUR", "0.76")
    }

    @Test
    fun `is orders JSON deserialized properly`() {
        val order: Order = mapper.readValue(mockOrder)
        order shouldBe Order("T2006", "10.00", "USD")
    }
}