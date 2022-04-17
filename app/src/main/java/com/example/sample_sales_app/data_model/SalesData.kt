package com.example.sample_sales_app.data_model

enum class Currency {
    EUR, USD, AUD, CAD
}

data class CurrencyChange(
    val from: Currency,
    val to: Currency,
    val rate: String
)

data class Order(
    val sku: String,
    val amount: String,
    val currency: Currency
)

data class CacheData(
    val currencyChanges: List<CurrencyChange> = emptyList(),
    val orders: List<Order> = emptyList()
)