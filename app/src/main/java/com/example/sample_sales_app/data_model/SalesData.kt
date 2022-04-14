package com.example.sample_sales_app.data_model

enum class HttpUrls(val string: String) {
    CURRENCIES_URL("https://quiet-stone-2094.herokuapp.com/rates.json"),
    ORDERS_URL("https://quiet-stone-2094.herokuapp.com/transactions.json")
}

data class CurrencyChange(
    val from: String,
    val to: String,
    val rate: String
)

data class Order(
    val sku: String,
    val amount: String,
    val currency: String
)

abstract class CacheData {
    abstract val currencyChanges: List<CurrencyChange>
    abstract val orders: List<Order>
}