package com.example.sample_sales_app.get_data

import com.example.sample_sales_app.data_model.CacheData
import com.example.sample_sales_app.data_model.Currency
import com.example.sample_sales_app.data_model.CurrencyChange
import com.example.sample_sales_app.data_model.Order
import com.example.sample_sales_app.utils.deserialize
import com.example.sample_sales_app.utils.hasChange
import com.example.sample_sales_app.utils.hasInverseChange
import com.example.sample_sales_app.view_model.MainViewModelInstance
import timber.log.Timber

object Cache {
    /**
     * Perform REST call only if data is missing
     */
    suspend fun get(): CacheData {
        val cache = MainViewModelInstance.state.value.cache

        return if (cache.currencyChanges.isEmpty() || cache.orders.isEmpty()) {
            getNewCache()
        } else cache
    }

    private suspend fun getNewCache(): CacheData {
        val currenciesRequest: RestResult =
            RestCall.callFor(HttpUrls.CURRENCIES_URL)
        val ordersRequest: RestResult =
            RestCall.callFor(HttpUrls.ORDERS_URL)

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
                if (!currenciesData.hasChange(from, to) || !currenciesData.hasInverseChange(from, to))
                    result.add(getCurrencyChangeFor(from, to, currenciesData))
        }
        return result.toList()
    }

    private fun getCurrencyChangeFor(
        from: Currency,
        to: Currency,
        currencyChanges: List<CurrencyChange>
    ): CurrencyChange {
        val rate = rateCalculator(from, to, currencyChanges)
        return CurrencyChange(
            from,
            to,
            rate
        )
    }

    private fun rateCalculator(
        from: Currency,
        to: Currency,
        currencyChanges: List<CurrencyChange>
    ): String {
        var rate: String

        // Check if the rate can be obtained by direct multiplication
        var fromMatches = currencyChanges.filter { it.from == from }
        var toMatches = currencyChanges.filter { it.to == to }
        rate = calculateRateOneMissingStep(fromMatches, toMatches)
        if (rate.isNotEmpty()) return rate

        // Check if the rate can be obtained by inverting one change
        fromMatches = currencyChanges.filter { it.to == from }
        rate = calculateRateOneMissingStep(
            fromMatches,
            toMatches,
            ConversionCase.INVERSE_FROM
        )
        if (rate.isNotEmpty()) return rate

        // Check if the rate can be obtained by inverting the other
        fromMatches = currencyChanges.filter { it.from == from }
        toMatches = currencyChanges.filter { it.from == to }
        rate = calculateRateOneMissingStep(
            fromMatches,
            toMatches,
            ConversionCase.INVERSE_TO
        )
        if (rate.isNotEmpty()) return rate

        // Check if the rate can be obtained by inverting both
        fromMatches = currencyChanges.filter { it.to == from }
        toMatches = currencyChanges.filter { it.from == to }
        rate = calculateRateOneMissingStep(
            fromMatches,
            toMatches,
            ConversionCase.INVERSE
        )
        return rate
    }

    private fun calculateRateOneMissingStep(
        fromMatches: List<CurrencyChange>,
        toMatches: List<CurrencyChange>,
        conversionCase: ConversionCase = ConversionCase.DIRECT
    ): String {
        var rate: Double? = null
        if (fromMatches.isEmpty() || toMatches.isEmpty()) {
            Timber.e(
                "Empty match list:\n FromMatches: $fromMatches\nToMatches: $toMatches"
            )
            return ""
        }

        fromMatches.forEach { fromMatch ->
            when (conversionCase) {
                ConversionCase.DIRECT -> toMatches.find { toMatch -> fromMatch.to == toMatch.from }
                    ?.let { toMatch ->
                        rate = fromMatch.rate.toDouble() * toMatch.rate.toDouble()
                        return@forEach
                    }
                ConversionCase.INVERSE_TO -> toMatches.find { toMatch -> fromMatch.to == toMatch.to }
                    ?.let { toMatch ->
                        rate = fromMatch.rate.toDouble() * (1 / toMatch.rate.toDouble())
                        return@forEach
                    }
                ConversionCase.INVERSE_FROM -> toMatches.find { toMatch -> fromMatch.from == toMatch.from }
                    ?.let { toMatch ->
                        rate = (1 / fromMatch.rate.toDouble()) * toMatch.rate.toDouble()
                        return@forEach
                    }
                ConversionCase.INVERSE -> toMatches.find { toMatch -> fromMatch.from == toMatch.to }
                    ?.let { toMatch ->
                        rate = (1 / fromMatch.rate.toDouble()) * (1 / toMatch.rate.toDouble())
                        return@forEach
                    }
            }
        }
        return rate?.toString()
            ?: ""
    }
}

private enum class ConversionCase {
    DIRECT, INVERSE_FROM, INVERSE_TO, INVERSE
}