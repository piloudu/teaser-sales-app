package com.example.sample_sales_app.get_data

import com.example.sample_sales_app.utils.toastMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import timber.log.Timber

object RestCall {
    private val client = OkHttpClient()

    suspend fun restCallFor(url: HttpUrls): RestResult {
        val restResult = try {
            withContext(Dispatchers.IO) {
                val request = Request.Builder().url(url.string).build()
                val result = client.newCall(request).execute().body?.string()

                result?.let {
                    RestResult(RestStatus.SUCCESS, result)
                } ?: RestResult(
                    RestStatus.NULL_REST_CALL_BODY,
                    RestCallError.NULL_REST_CALL_BODY.message
                )
            }
        } catch (e: Exception) {
            Timber.e(e)
            RestResult(RestStatus.BAD_REQUEST, RestCallError.BAD_REQUEST.message)
        }
        restResult.isSuccess()
        return restResult
    }

    enum class RestStatus {
        SUCCESS, NULL_REST_CALL_BODY, BAD_REQUEST
    }
}

data class RestResult(val status: RestCall.RestStatus, val message: String) {

    suspend fun isSuccess(): Boolean {
        return if (status != RestCall.RestStatus.SUCCESS) {
            Timber.e(message)
            withContext(Dispatchers.Main) {
                toastMessage(message)
            }
            false
        } else true
    }
}

enum class HttpUrls(val string: String) {
    CURRENCIES_URL("https://quiet-stone-2094.herokuapp.com/rates.json"),
    ORDERS_URL("https://quiet-stone-2094.herokuapp.com/transactions.json")
}

enum class RestCallError(val message: String) {
    NULL_REST_CALL_BODY("Null call request body"),
    BAD_REQUEST("Bad request, check connection or request URL")
}