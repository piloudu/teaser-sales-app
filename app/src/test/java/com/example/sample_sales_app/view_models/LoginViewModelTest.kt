package com.example.sample_sales_app.view_models

import com.example.sample_sales_app.view_models.LoginViewModel.*
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.setMain
import org.junit.Test
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested


class LoginViewModelTest {
    val dispatcher = TestCoroutineDispatcher()
    val mockJson = """{"example": true, "example1": 5}"""

    @BeforeEach
    fun initViewModel() {
    }

    @Test
    fun `is REST call result for currencyRequest not an empty JSON`() {
        runBlocking {
            Dispatchers.setMain(TestCoroutineDispatcher())
            val loginViewModel = LoginViewModel()
            val result = loginViewModel.performRestCall(loginViewModel.currencyRequest)
            println("Result: ${result.status.name}")
            result.status shouldNotBe RestStatus.EMPTY_JSON
        }
    }

    @Nested
    inner class CurrencyRetrieveTests {
        private lateinit var result: RestResult

        @BeforeEach
        fun init() {
            runBlocking {
                launch(Dispatchers.Main) {
                    //result = loginViewModel.performRestCall(loginViewModel.currencyRequest)
                }
            }
            println("Result: ${result.status.name}")
        }

        @Test
        fun `is REST call result for currencyRequest not an empty JSON`() {
            result.status shouldNotBe RestStatus.EMPTY_JSON
        }

        //@Test
        //fun `is REST call result not connection error`() {
        //    result.status shouldNotBe RestStatus.NO_CONNECTION
        //}

        //@Test
        //fun `is REST call result success`() {
        //    result.status shouldBe RestStatus.SUCCESS
        //}
    }


    @Test
    fun `is the JSON deserialized`() {
    }
}