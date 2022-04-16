package com.example.sample_sales_app.utils

import android.widget.Toast
import com.example.sample_sales_app.MainActivity
import com.example.sample_sales_app.data_model.Currency
import com.example.sample_sales_app.get_data.Cache
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

internal val mapper = jacksonObjectMapper()
internal fun <T> String.deserialize(): List<T> = mapper.readValue(this)

fun toastMessage(message: String) {
    Toast.makeText(MainActivity.getContext(), message, Toast.LENGTH_SHORT).show()
}

suspend infix fun Currency.to(currency: Currency): String {
    val currencyChanges = Cache.get().currencyChanges
    var rate = ""
    currencyChanges.find { it.from == this.name && it.to == currency.name }?.let {
        return it.rate
    }
    currencyChanges.find { it.from == currency.name && it.to == this.name }?.let {
        rate = (1/it.rate.toDouble()).toString()
    }
    return rate
}