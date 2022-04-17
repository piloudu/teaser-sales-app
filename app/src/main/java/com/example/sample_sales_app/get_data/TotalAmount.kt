package com.example.sample_sales_app.get_data

import com.example.sample_sales_app.data_model.CacheData
import com.example.sample_sales_app.data_model.Currency
import com.example.sample_sales_app.utils.getRate
import java.math.RoundingMode
import java.text.DecimalFormat

object TotalAmount {
    fun getFor(
        orderCode: String? = null,
        targetCurrency: Currency? = null,
        cache: CacheData
    ): String {
        if (orderCode == null || targetCurrency == null) return ""
        if (cache.currencyChanges.isEmpty() || cache.orders.isEmpty()) return ""
        return calculateTotalAmount(orderCode, targetCurrency, cache)
    }

    private fun calculateTotalAmount(
        orderCode: String,
        targetCurrency: Currency,
        cache: CacheData
    ): String {
        val matchingOrders = cache.orders.filter { it.sku == orderCode }
        val amountToCurrencyMap = matchingOrders.associate {
            it.currency to it.amount
        }
        var totalAmount: Double = 0.0
        for ((currency, amount) in amountToCurrencyMap) {
            if (currency == targetCurrency) {
                totalAmount += amount.toDouble()
                continue
            }
            val currencyRateToTarget = cache.currencyChanges.getRate(currency, targetCurrency)
            if (currencyRateToTarget.isEmpty()) continue
            else totalAmount += currencyRateToTarget.toDouble() * amount.toDouble()
        }
        return formatTotalAmount(totalAmount)
    }

    private fun formatTotalAmount(totalAmount: Double): String {
        val decimalFormat = DecimalFormat("#.##")
        decimalFormat.roundingMode = RoundingMode.HALF_UP

        return decimalFormat.format(totalAmount).replace(".", ",")
    }

}