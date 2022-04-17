package com.example.sample_sales_app

import com.example.sample_sales_app.data_model.Currency.*
import com.example.sample_sales_app.data_model.Order
import com.example.sample_sales_app.utils.deserialize
import io.kotest.matchers.shouldBe
import org.junit.Rule
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class TotalAmountTest {
    private val sampleOrder = """ 
        [{"sku": "T2006", "amount": "10.00", "currency": "USD"},
        {"sku": "T2006", "amount": "7.63", "currency": "EUR"}]
        """.trimIndent()

    @DisplayName("Retrieves the data for")
    @Nested
    inner class RetrieveData {

        @get:Rule
        val ordersData: List<Order> = sampleOrder.deserialize()

        @DisplayName("first order")
        @Test
        fun `properly retrieves first order data`() {
            ordersData[0] shouldBe Order("T2006", "10.00", USD)
        }

        @DisplayName("second order")
        @Test
        fun `properly retrieves first second data`() {
            val ordersData: List<Order> = sampleOrder.deserialize()
            ordersData[1] shouldBe Order("T2006", "7.63", EUR)
        }
    }

    
}