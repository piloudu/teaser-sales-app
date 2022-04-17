package com.example.sample_sales_app.get_data

import com.example.sample_sales_app.data_model.CacheData
import com.example.sample_sales_app.data_model.Currency

object TotalAmount {
    fun getFor(
        orderCode: String? = null,
        targetCurrency: Currency? = null,
        cache: CacheData
    ): String {
        if (orderCode == null || targetCurrency == null) return ""
        if (cache.currencyChanges.isEmpty() || cache.orders.isEmpty()) return ""
        return ""
    }

}