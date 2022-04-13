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

internal val client = OkHttpClient()
internal val currencyRequest = Request.Builder().url(CURRENCIES_URL).build()
internal val ordersRequest = Request.Builder().url(ORDERS_URL).build()

internal const val TAG = "LOGIN"
    enum class LoginStatus {
    IDLE, LOADING, LOADED, ERROR
}

data class LoginState(
    val loginStatus: LoginStatus = LoginStatus.IDLE
) : State

object LoginIntent : MviIntent

sealed class LoginReduceAction : ReduceAction {
    data class Loading(val message: String = "LOADING") : LoginReduceAction()
    data class Loaded(val message: String = "LOADED") : LoginReduceAction()
    data class Error(val message: String = "ERROR") : LoginReduceAction()
}

class LoginViewModel : MviViewModel<LoginState, LoginIntent, LoginReduceAction>(LoginState()) {
    /**
     * On Login, try to download and cache the data
     */
    override suspend fun executeIntent(mviIntent: LoginIntent) {
        val currencyDate = getRestCallDataOrNull(currencyRequest)
        val ordersData = getRestCallDataOrNull(ordersRequest)
        //handle(LoginReduceAction)
    }

    override suspend fun reduce(state: LoginState, reduceAction: LoginReduceAction): LoginState {
        return state
    }

    private suspend fun getRestCallDataOrNull(request: Request): String? {
        return try {
            withContext(Dispatchers.IO) {
                val response = client.newCall(request).execute()
                response.body?.string() ?: run {
                    getErrorMessageFor(LoginError.EMPTY_JSON)
                    null
                }
            }
        } catch (e: IllegalStateException) {
            getErrorMessageFor(LoginError.NO_CONNECTION)
            null
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
}

enum class LoginError(val message: String) {
    EMPTY_JSON("Empty JSON retrieved"),
    NO_CONNECTION("You have no internet connection")
}
