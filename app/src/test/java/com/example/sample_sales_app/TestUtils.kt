package com.example.sample_sales_app

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain

private val dispatcher = TestCoroutineDispatcher()

internal fun withTestScope(scope: suspend CoroutineScope.() -> Unit) {
    runBlocking {
        Dispatchers.setMain(dispatcher)
        scope()
    }
}
val mockOrders = """ 
    [{ "sku": "T2006", "amount": "10.00", "currency": "USD" },
     { "sku": "M2007", "amount": "34.57", "currency": "CAD" },
     { "sku": "R2008", "amount": "17.95", "currency": "USD" },
     { "sku": "T2006", "amount":  "7.63", "currency": "EUR" }, 
     { "sku": "B2009", "amount": "21.23", "currency": "USD" }]
""".trimIndent()

val mockCurrencyChanges = """
    [{ "from": "EUR", "to": "USD", "rate": "1.359" },
     { "from": "CAD", "to": "EUR", "rate": "0.732" },
     { "from": "USD", "to": "EUR", "rate": "0.736" },
     { "from": "EUR", "to": "CAD", "rate": "1.366" }]
""".trimIndent()
