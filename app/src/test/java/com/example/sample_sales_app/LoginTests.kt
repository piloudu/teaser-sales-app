package com.example.sample_sales_app

import com.example.sample_sales_app.data_model.CacheData
import com.example.sample_sales_app.data_model.Currency.*
import com.example.sample_sales_app.data_model.CurrencyChange
import com.example.sample_sales_app.data_model.Order
import com.example.sample_sales_app.get_data.*
import com.example.sample_sales_app.utils.deserialize
import com.example.sample_sales_app.utils.mapper
import com.example.sample_sales_app.view_model.MainViewModel
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.Rule
import org.junit.jupiter.api.*

class LoginTests {
    private lateinit var result: RestResult

    @DisplayName("Should retrieve currency JSON")
    @Test
    fun `is REST call result for currencyRequest a success`() {
        withTestScope {
            result = RestCall.restCallFor(HttpUrls.CURRENCIES_URL)
            println(result.message)
            result.status shouldBe RestCall.RestStatus.SUCCESS
        }
    }

    @DisplayName("Should retrieve orders JSON")
    @Test
    fun `is REST call result for ordersRequest a success`() {
        withTestScope {
            result = RestCall.restCallFor(HttpUrls.ORDERS_URL)
            result.status shouldBe RestCall.RestStatus.SUCCESS
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("Retrieves currencies data for")
    @Nested
    inner class DeserializeCurrencyChange {

        @BeforeAll
        fun init() {
            withTestScope {
                result = RestCall.restCallFor(HttpUrls.CURRENCIES_URL)
            }
        }

        @DisplayName("CAD to EUR")
        @Test
        fun `properly retrieves first currency change`() {
            val currencyChange: List<CurrencyChange> = mapper.readValue(mockCurrencyChanges)
            currencyChange[1] shouldBe CurrencyChange(CAD, EUR, "0.732")
        }

        @DisplayName("USD to EUR")
        @Test
        fun `properly retrieves second currency change`() {
            val currencyChange: List<CurrencyChange> = mapper.readValue(mockCurrencyChanges)
            currencyChange[2] shouldBe CurrencyChange(USD, EUR, "0.736")
        }

        @DisplayName("all of them")
        @Test
        fun `is currency JSON deserializer properly`() {
            val currencyChanges: List<CurrencyChange> = result.message.deserialize()
            currencyChanges.size shouldBe 6
        }
    }

    @DisplayName("Retrieves order data for")
    @Nested
    inner class DeserializeOrder {
        @get:Rule
        val ordersData: List<Order> = mockOrders.deserialize()

        @DisplayName("M2007 order")
        @Test
        fun `properly retrieves M2007 order data`() {
            val order = ordersData.find { it.sku == "M2007" }
            order shouldBe Order("M2007", "34.57", CAD)
        }

        @DisplayName("B2009 order")
        @Test
        fun `properly retrieves B2009 order data`() {
            val order = ordersData.find { it.sku == "B2009" }
            order shouldBe Order("B2009", "21.23", USD)
        }
    }

    @Nested
    inner class CacheDataTest {
        @DisplayName("is Cache created properly")
        @Test
        fun `is Cache created properly`() {
            withTestScope {
                val cache = Cache.get()
                cache.shouldBeInstanceOf<CacheData>()
            }
        }

        @DisplayName("is Cache always the same object")
        @Test
        fun `is Cache always the same object`() {
            withTestScope {
                val cache = MainViewModel.state.value.cache
                val cache1 = MainViewModel.state.value.cache
                cache.hashCode() shouldBe cache1.hashCode()
            }
        }
    }
}