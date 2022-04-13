package com.example.sample_sales_app.view_models

import android.widget.Toast
import com.example.sample_sales_app.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import timber.log.Timber

internal const val CURRENCIES_URL = "http://quiet-stone-2094.herokuapp.com/rates.json"
internal const val ORDERS_URL = "http://quiet-stone-2094.herokuapp.com/transactions.json"

enum class LoginError(val message: String) {
    NULL_REST_CALL_BODY("Null call request body"),
    BAD_REQUEST("Bad request, check connection or request URL")
}

internal const val TAG = "LOGIN"

enum class LoginStatus {
    IDLE, LOADING, LOADED, ERROR
}

data class LoginState(
    val loginStatus: LoginStatus = LoginStatus.IDLE
) : State

object LoginIntent : MviIntent

sealed class LoginReduceAction : ReduceAction {
    data class Loading(val message: String = LoginStatus.LOADING.name) : LoginReduceAction()
    data class Loaded(val message: String = LoginStatus.LOADED.name) : LoginReduceAction()
    data class Error(val message: String = LoginStatus.ERROR.name) : LoginReduceAction()
}

class LoginViewModel : MviViewModel<LoginState, LoginIntent, LoginReduceAction>(LoginState()) {
    private val client = OkHttpClient()
    val currencyRequest = Request.Builder().url(CURRENCIES_URL).build()
    val ordersRequest = Request.Builder().url(ORDERS_URL).build()

    /**
     * On Login, try to download and cache the data
     */
    override suspend fun executeIntent(mviIntent: LoginIntent) {
        val currencyDate = performRestCall(currencyRequest)
        val ordersData = performRestCall(ordersRequest)
        if (currencyDate.isSuccess() && ordersData.isSuccess()) {
            //handle(LoginReduceAction)
        }
    }

    override suspend fun reduce(state: LoginState, reduceAction: LoginReduceAction): LoginState {
        return state
    }

    suspend fun performRestCall(request: Request): RestResult {
        return try {
            withContext(Dispatchers.IO) {
                val response = client.newCall(request).execute()
                val result = response.body?.string()
                result?.let {
                    RestResult(RestStatus.SUCCESS, result)
                } ?: RestResult(RestStatus.NULL_REST_CALL_BODY, LoginError.NULL_REST_CALL_BODY.message)
            }
        } catch (e: Exception) {
            RestResult(RestStatus.BAD_REQUEST, LoginError.BAD_REQUEST.message)
        }
    }

    private fun RestResult.isSuccess(): Boolean {
        return if (status != RestStatus.SUCCESS) {
            Timber.tag(TAG).e(message)
            Toast.makeText(MainActivity.getContext(), message, Toast.LENGTH_SHORT).show()
            false
        } else true
    }

    data class RestResult(val status: RestStatus, val message: String)

    enum class RestStatus {
        SUCCESS, NULL_REST_CALL_BODY, BAD_REQUEST
    }

}
