package com.example.sample_sales_app

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class TotalAmountTest {
    val sampleOrder = """ 
        {"sku": "T2006", "amount": "10.00", "currency": "USD"},
        {"sku": "T2006", "amount": "7.63", "currency": "EUR"}
        """.trimIndent()

    @DisplayName("Retrieves the data for sampleOrder")
    @Test
    fun `properly retrieves the data`() {

    }
}