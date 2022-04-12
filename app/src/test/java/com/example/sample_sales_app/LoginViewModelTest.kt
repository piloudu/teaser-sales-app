package com.example.sample_sales_app

import junit.framework.Assert.assertNotNull
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.Test

val client = OkHttpClient()

const val CURRENCIES_URL = "http://quiet-stone-2094.herokuapp.com/rates.json"
const val ORDERS_URL = "http://quiet-stone-2094.herokuapp.com/transactions.json"

val request = Request.Builder()
    .url(CURRENCIES_URL).build()

class LoginViewModelTest {
    @Test
    fun `is REST call performed`() {
        val result = client.newCall(request)
        assertNotNull(result)
    }
}