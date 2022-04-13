package com.example.sample_sales_app.view_models

import com.example.sample_sales_app.view_models.LoginViewModel.*
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Test


class LoginViewModelTest {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var result: RestResult
    val dispatcher = TestCoroutineDispatcher()
    val mockJson = """{"example": true, "example1": 5}"""

    fun withScopeInitializer(scope: suspend CoroutineScope.() -> Unit) {
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
}