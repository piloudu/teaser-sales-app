package com.example.sample_sales_app.view_model

import com.example.sample_sales_app.data_model.Currency.*
import com.example.sample_sales_app.utils.changeTo
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class CurrencyChangeTest {

    @DisplayName("USD to EUR")
    @Test
    fun `is USD converted to EUR`() {
        val rate = "1.02"
        withTestScope {
            USD changeTo EUR shouldBe rate
        }
    }

    @DisplayName("CAD to EUR")
    @Test
    fun `is CAD converted to EUR`() {
        val rate = (1/.79).toString()
        withTestScope {
            CAD changeTo EUR shouldBe rate
        }
    }

    @DisplayName("AUD to EUR")
    @Test
    fun `is AUD converted to EUR`() {
        val rate = (1.15*1.02).toString()
        withTestScope {
            AUD changeTo EUR shouldBe rate
        }
    }

    @DisplayName("CAD to USD")
    @Test
    fun `is CAD converted to USD`() {
        val rate = (1.27*.98).toString()
        withTestScope {
            CAD changeTo USD shouldBe rate
        }
    }

    @DisplayName("CAD to AUD")
    @Test
    fun `is CAD converted to AUD`() {
        val rate = ((1/.79)*.98*.87).toString()
        withTestScope {
            CAD changeTo AUD shouldBe rate
        }
    }

    @DisplayName("AUD to USD")
    @Test
    fun `is AUD converted to USD`() {
        val rate = "0.76"
        withTestScope {
            AUD changeTo USD shouldBe rate
        }
    }
}