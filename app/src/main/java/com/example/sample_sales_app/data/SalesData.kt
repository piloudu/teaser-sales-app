package com.example.sample_sales_app.data

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

data class Cache(
    val currencyChanges: List<CurrencyChange>,
    val orders: List<Order>
)