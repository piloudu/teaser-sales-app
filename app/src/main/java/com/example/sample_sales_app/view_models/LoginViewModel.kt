package com.example.sample_sales_app.view_models

import android.widget.Toast
import com.example.sample_sales_app.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import timber.log.Timber

internal const val CURRENCIES_URL = "http://quiet-stone-2094.herokuapp.com/rates.json"
internal const val ORDERS_URL = "http://quiet-stone-2094.herokuapp.com/transactions.json"

enum class LoginError(val message: String) {
    EMPTY_JSON("Empty JSON retrieved"),
    NO_CONNECTION("You have no internet connection")
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
        //handle(LoginReduceAction)
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
                } ?: RestResult(RestStatus.EMPTY_JSON, LoginError.EMPTY_JSON.message)
            }
        } catch (e: IllegalStateException) {
            RestResult(RestStatus.NO_CONNECTION, LoginError.NO_CONNECTION.message)
        }
    }

    private fun getErrorMessageFor(loginError: LoginError) {
        val message = loginError.message
        when (loginError) {
            LoginError.NO_CONNECTION -> errorPrompt(message)
            LoginError.EMPTY_JSON -> errorPrompt(message)
        }
    }

    private fun errorPrompt(message: String) {
        Timber.tag(TAG).e(message)
        Toast.makeText(MainActivity.getContext(), message, Toast.LENGTH_SHORT).show()
    }

    data class RestResult(val status: RestStatus, val message: String)

    enum class RestStatus {
        SUCCESS, EMPTY_JSON, NO_CONNECTION
    }

}
