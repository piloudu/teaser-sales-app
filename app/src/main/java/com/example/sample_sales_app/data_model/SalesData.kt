package com.example.sample_sales_app.data_model

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