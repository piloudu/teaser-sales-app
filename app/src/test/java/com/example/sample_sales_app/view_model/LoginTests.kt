package com.example.sample_sales_app.view_model

import com.example.sample_sales_app.data_model.CacheData
import com.example.sample_sales_app.data_model.CurrencyChange
import com.example.sample_sales_app.data_model.Order
import com.example.sample_sales_app.get_data.*
import com.example.sample_sales_app.utils.deserialize
import com.example.sample_sales_app.utils.mapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.*

class LoginTests {
    private lateinit var result: RestResult
    private val dispatcher = TestCoroutineDispatcher()

    private fun withLoginScope(scope: suspend CoroutineScope.() -> Unit) {
        runBlocking {
            Dispatchers.setMain(dispatcher)
            scope()
        }
    }

    @DisplayName("Should retrieve currency JSON")
    @Test
    fun `is REST call result for currencyRequest a success`() {
        withLoginScope {
            result = RestCall.restCallFor(HttpUrls.CURRENCIES_URL)
            println(result.message)
            result.status shouldBe RestCall.RestStatus.SUCCESS
        }
    }

    @DisplayName("Should retrieve orders JSON")
    @Test
    fun `is REST call result for ordersRequest a success`() {
        withLoginScope {
            result = RestCall.restCallFor(HttpUrls.ORDERS_URL)
            result.status shouldBe RestCall.RestStatus.SUCCESS
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("On JSON retrieve")
    @Nested
    inner class DeserializeCurrencyChange {
        private val mockCurrencyChange = """{"from": "CAD", "to": "EUR", "rate": "0.76"}"""

        @BeforeAll
        fun init() {
            withLoginScope {
                result = RestCall.restCallFor(HttpUrls.CURRENCIES_URL)
            }
        }

        @DisplayName("should deserialize mock individual element")
        @Test
        fun `is currency sample deserialized properly`() {
            val currencyChange: CurrencyChange = mapper.readValue(mockCurrencyChange)
            currencyChange shouldBe CurrencyChange("CAD", "EUR", "0.76")
        }

        @DisplayName("should deserialize the currency change list")
        @Test
        fun `is currency JSON deserializer properly`() {
            val currencyChanges: List<CurrencyChange> = result.message.deserialize()
            currencyChanges.size shouldBe 6
        }
    }

    @Nested
    inner class DeserializeOrder {
        private val mockOrder = """{"sku": "T2006", "amount": "10.00", "currency": "USD"}"""

        @DisplayName("should deserialize mock individual element")
        @Test
        fun `is orders JSON deserialized properly`() {
            val order: Order = mapper.readValue(mockOrder)
            order shouldBe Order("T2006", "10.00", "USD")
        }
    }

    @Nested
    inner class CacheDataTest {
        @DisplayName("is Cache created properly")
        @Test
        fun `is Cache created properly`() {
            withLoginScope {
                val cache = Cache.get()
                cache.shouldBeInstanceOf<CacheData>()
            }
        }

        @DisplayName("is Cache always the same object")
        @Test
        fun `is Cache always the same object`() {
            withLoginScope {
                val cache = MainViewModel.state.value.cache
                val cache1 = MainViewModel.state.value.cache
                cache.hashCode() shouldBe cache1.hashCode()
            }
        }
    }
}