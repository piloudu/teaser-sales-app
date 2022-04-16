package com.example.sample_sales_app.view_model

import androidx.lifecycle.viewModelScope
import com.example.sample_sales_app.data_model.CacheData
import com.example.sample_sales_app.data_model.Currency.*
import com.example.sample_sales_app.data_model.CurrencyChange
import com.example.sample_sales_app.get_data.Cache
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.launch
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class CurrencyChangeTest {
    private lateinit var cache: CacheData
    private lateinit var currencyChangeList: List<CurrencyChange>

    @BeforeEach
    fun getData() {
        MainViewModel.viewModelScope.launch {
            cache = Cache.get()
            currencyChangeList = cache.currencyChanges
        }
    }

    @DisplayName("USD to EUR")
    @Test
    fun `is USD converted to EUR`() {
        val rate = "1.02"
        EUR to USD shouldBe rate
    }

    @DisplayName("CAD to EUR")
    @Test
    fun `is CAD converted to EUR`() {
        val rate = (1/.79).toString()
        CAD to EUR shouldBe rate
    }

    @DisplayName("AUD to EUR")
    @Test
    fun `is AUD converted to EUR`() {
        val rate = (1.15*1.02).toString()
        AUD to EUR shouldBe rate
    }

    @DisplayName("CAD to USD")
    @Test
    fun `is CAD converted to USD`() {
        val rate = (1.27*.98).toString()
        CAD to USD shouldBe rate
    }

    @DisplayName("CAD to AUD")
    @Test
    fun `is CAD converted to AUD`() {
        val rate = ((1/.79)*.98*.87).toString()
        CAD to AUD shouldBe rate
    }

    @DisplayName("AUD to USD")
    @Test
    fun `is AUD converted to USD`() {
        val rate = "0.76"
        AUD to USD shouldBe rate
    }
}