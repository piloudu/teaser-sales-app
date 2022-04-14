package com.example.sample_sales_app.get_data

import com.example.sample_sales_app.data_model.CacheData
import com.example.sample_sales_app.data_model.CurrencyChange
import com.example.sample_sales_app.data_model.Order
import com.example.sample_sales_app.view_model.MainViewModel

object Cache {
    /**
     * Perform REST call only if data is missing
     */
    suspend fun get(): CacheData {
        val cache = MainViewModel.state.value.cache

        return if (cache.currencyChanges.isEmpty() || cache.orders.isEmpty()) {
            getNewCache()
        } else cache
    }

    private suspend fun getNewCache(): CacheData {
        val currenciesRequest: RestResult =
            RestCall.restCallFor(HttpUrls.CURRENCIES_URL)
        val ordersRequest: RestResult =
            RestCall.restCallFor(HttpUrls.ORDERS_URL)

        return if (currenciesRequest.isSuccess() && ordersRequest.isSuccess()) {
            val currenciesData: List<CurrencyChange> = currenciesRequest.message.deserialize()
            val ordersData: List<Order> = ordersRequest.message.deserialize()
            CacheData(
                currenciesData,
                ordersData
            )
        } else CacheData()
    }
}