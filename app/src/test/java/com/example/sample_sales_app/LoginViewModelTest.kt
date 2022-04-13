package com.example.sample_sales_app

import com.example.sample_sales_app.view_models.*
import io.kotest.matchers.shouldNotBe
import org.junit.Test


class LoginViewModelTest {
    val mockJson = """{"example": true, "example1": 5}"""

    @Test
    fun `is REST call performed`() {
        val result = try {
            val response = client.newCall(currencyRequest).execute()
            val callResult = response.body?.string()
            println("ThisResult: $callResult")
            callResult
        } catch (e: Exception) {
            LoginError.NO_CONNECTION.message
        }
        result shouldNotBe LoginError.NO_CONNECTION.message
    }

    @Test
    fun `is the JSON deserialized`() {
    }
}