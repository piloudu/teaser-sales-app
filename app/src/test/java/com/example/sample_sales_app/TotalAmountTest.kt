package com.example.sample_sales_app

import com.example.sample_sales_app.data_model.CacheData
import com.example.sample_sales_app.data_model.Currency.*
import com.example.sample_sales_app.data_model.CurrencyChange
import com.example.sample_sales_app.data_model.Order
import com.example.sample_sales_app.get_data.TotalAmount
import com.example.sample_sales_app.utils.deserialize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class TotalAmountTest {
    private val orders: List<Order> = mockOrders.deserialize()
    private val currencyChanges: List<CurrencyChange> = mockCurrencyChanges.deserialize()

    val cache = CacheData(
        orders = orders,
        currencyChanges = currencyChanges
    )

    @DisplayName("The total amount is empty if lacking")
    @Nested
    inner class LackingInformation {
        @DisplayName("currency information")
        @Test
        fun `null currency`() {
            val orderCode = orders[0].sku
            val result = TotalAmount.getFor(orderCode = orderCode, cache = cache)
            result shouldBe ""
        }

        @DisplayName("orderCode information")
        @Test
        fun `null orderCode`() {
            val targetCurrency = EUR
            val result = TotalAmount.getFor(targetCurrency = EUR, cache = cache)
            result shouldBe ""
        }

        @DisplayName("all information")
        @Test
        fun `all null`() {
            val result = TotalAmount.getFor(cache = cache)
            result shouldBe ""
        }
    }

    @DisplayName("The total amount is")
    @Nested
    inner class TotalAmountResult {
        private lateinit var result: String

        @BeforeEach
        fun computeResult() {
            val orderCode = orders[0].sku
            val targetCurrency = EUR
            result = TotalAmount.getFor(
                orderCode = orderCode,
                targetCurrency = targetCurrency,
                cache = cache
            )
        }

        @DisplayName("not null")
        @Test
        fun `the total amount is calculated`() {
            result shouldNotBe ""
        }

        @DisplayName("calculated properly")
        @Test
        fun `the total amount is calculated properly`() {
            result shouldBe "14,99"
        }

        @DisplayName("rounded properly")
        @Test
        fun `the total amount is rounded properly`() {
            val result = TotalAmount.getFor(
                orderCode = "B2009",
                targetCurrency = EUR,
                cache = cache
            )
            result shouldBe "15,63"
        }
    }
}