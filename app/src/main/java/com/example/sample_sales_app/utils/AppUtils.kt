package com.example.sample_sales_app.utils

import android.widget.Toast
import com.example.sample_sales_app.MainActivity
import com.example.sample_sales_app.data_model.Currency
import com.example.sample_sales_app.data_model.CurrencyChange
import com.example.sample_sales_app.get_data.Cache
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

internal val mapper = jacksonObjectMapper()
internal inline fun <reified T> String.deserialize(): List<T> = mapper.readValue(
    this,
    object: TypeReference<List<T>>() {}
)

fun toastMessage(message: String) {
    Toast.makeText(MainActivity.getContext(), message, Toast.LENGTH_SHORT).show()
}

/**
 * Get the conversion between two currencies or an empty string if it couldn't be calculated
 */
suspend infix fun Currency.changeToOrEmpty(currency: Currency): String {
    val currencyChanges = Cache.get().currencyChanges
    var rate = ""
    currencyChanges.find { it.from == this.name && it.to == currency.name }?.takeIf {
        it.rate.isNotEmpty() }?.let {
        return it.rate
    }
    currencyChanges.find { it.from == currency.name && it.to == this.name }?.takeIf {
        it.rate.isNotEmpty() }?.let {
        rate = (1/it.rate.toDouble()).toString()
    }
    return rate
}

fun List<CurrencyChange>.hasChange(from: String, to: String): Boolean {
    return this.find { it.from == from && it.to == to }?.let {
        true
    } ?: false
}

fun List<CurrencyChange>.hasInverseChange(from: String, to: String): Boolean {
    return this.find { it.from == to && it.to == from }?.let {
        true
    } ?: false
}