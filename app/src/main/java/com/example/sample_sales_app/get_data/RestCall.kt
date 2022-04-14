package com.example.sample_sales_app.get_data

import android.widget.Toast
import com.example.sample_sales_app.MainActivity
import com.example.sample_sales_app.data_model.HttpUrls
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

    data class RestResult(val status: RestStatus, val message: String)

    enum class RestStatus {
        SUCCESS, NULL_REST_CALL_BODY, BAD_REQUEST
    }

    private fun RestResult.isSuccess(): Boolean {
        return if (status != RestStatus.SUCCESS) {
            Timber.e(message)
            Toast.makeText(MainActivity.getContext(), message, Toast.LENGTH_SHORT).show()
            false
        } else true
    }
}

enum class RestCallError(val message: String) {
    NULL_REST_CALL_BODY("Null call request body"),
    BAD_REQUEST("Bad request, check connection or request URL")
}