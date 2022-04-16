package com.example.sample_sales_app.view_model

import com.example.sample_sales_app.data_model.Currency.*
import com.example.sample_sales_app.utils.changeToOrEmpty
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class CurrencyChangeTest {

    @DisplayName("USD to EUR")
    @Test
    fun `is USD converted to EUR`() {
        withTestScope {
            USD changeToOrEmpty EUR shouldNotBe ""
        }
    }

    @DisplayName("CAD to EUR")
    @Test
    fun `is CAD converted to EUR`() {
        withTestScope {
            CAD changeToOrEmpty EUR shouldNotBe ""
        }
    }

    @DisplayName("AUD to EUR")
    @Test
    fun `is AUD converted to EUR`() {
        withTestScope {
            AUD changeToOrEmpty EUR shouldNotBe ""
        }
    }

    @DisplayName("CAD to USD")
    @Test
    fun `is CAD converted to USD`() {
        withTestScope {
            CAD changeToOrEmpty USD shouldNotBe ""
        }
    }

    @DisplayName("CAD to AUD")
    @Test
    fun `is CAD converted to AUD`() {
        withTestScope {
            CAD changeToOrEmpty AUD shouldNotBe ""
        }
    }

    @DisplayName("AUD to USD")
    @Test
    fun `is AUD converted to USD`() {
        withTestScope {
            AUD changeToOrEmpty USD shouldNotBe ""
        }
    }
}