package com.example.sample_sales_app.get_data

import com.example.sample_sales_app.data_model.CacheData
import com.example.sample_sales_app.data_model.Currency
import com.example.sample_sales_app.data_model.CurrencyChange
import com.example.sample_sales_app.data_model.Order
import com.example.sample_sales_app.utils.deserialize
import com.example.sample_sales_app.utils.factorial
import com.example.sample_sales_app.utils.hasChange
import com.example.sample_sales_app.utils.hasInverseChange
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
            val completeCurrencyChanges = getAllCurrencyChanges(currenciesData)

            CacheData(
                completeCurrencyChanges,
                ordersData
            )
        } else CacheData()
    }

    private fun getAllCurrencyChanges(currenciesData: List<CurrencyChange>): List<CurrencyChange> {
        val result = currenciesData.toMutableList()
        val currencies = Currency.values().toMutableList()
        val usedCurrencies = mutableListOf<Currency>()

        for (index in 0 until currencies.size) {
            val from = currencies.first()
            usedCurrencies.add(from)
            currencies.removeAll(usedCurrencies)
            for (to in currencies)
                if (!result.hasChange(from.name, to.name) || !result.hasInverseChange(from.name, to.name))
                    result.add(getCurrencyChangeFor(from, to, result))
        }
        return result.toList()
    }

    private fun getCurrencyChangeFor(
        from: Currency,
        to: Currency,
        currencyChanges: MutableList<CurrencyChange>
    ): CurrencyChange {
        var _rate: Double? = null
        val fromMatches = currencyChanges.filter { it.from == from.name }
        val toMatches = currencyChanges.filter { it.to == to.name }

        fromMatches.forEach { fromMatch ->
            toMatches.find { toMatch -> fromMatch.to == toMatch.from }?.let { toMatch ->
                _rate = fromMatch.rate.toDouble() * toMatch.rate.toDouble()
            }
        }

        val rate = _rate ?: throw(Exception("Undefined operation"))
        return CurrencyChange(
            from.name,
            to.name,
            rate.toString()
        )
    }

    private fun totalPossiblePairCombinations(sampleSize: Int): Int {
        return sampleSize.factorial() / (2 * (sampleSize - 2).factorial())
    }
}