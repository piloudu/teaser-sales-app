package com.example.sample_sales_app.view_models

import android.widget.Toast
import com.example.sample_sales_app.*
import com.example.sample_sales_app.data.CacheData
import com.example.sample_sales_app.data.CurrencyChange
import com.example.sample_sales_app.data.Order
import com.example.sample_sales_app.utils.deserialize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.Request
import timber.log.Timber

enum class LoginStatus {
    IDLE, LOADING, LOADED
}

data class LoginState(
    val loginStatus: LoginStatus = LoginStatus.IDLE,
) : State

object LoginIntent : MviIntent

sealed class LoginReduceAction(open val message: String = "") : ReduceAction {
    data class Loading(override val message: String = LoginStatus.LOADING.name) : LoginReduceAction()
    data class Loaded(override val message: String = LoginStatus.LOADED.name) : LoginReduceAction()
}

class LoginViewModel private constructor() : MviViewModel<LoginState, LoginIntent, LoginReduceAction>(LoginState()) {
    private val client = OkHttpClient()
    internal val currencyRequest = Request.Builder().url(CURRENCIES_URL).build()
    internal val ordersRequest = Request.Builder().url(ORDERS_URL).build()
    internal var currencyChangeList = listOf<CurrencyChange>()
    internal var ordersList = listOf<Order>()

    /**
     * On Login, try to download and cache the data
     */
    override suspend fun executeIntent(mviIntent: LoginIntent) {
        val action: LoginReduceAction
        getData()
        action = LoginReduceAction.Loaded()
        handle(action)
        Timber.tag(TAG).d("Action state is ${action.message}")
    }

    suspend fun getData() {
        val currencyData = performRestCall(currencyRequest)
        val ordersData = performRestCall(ordersRequest)
        currencyChangeList = currencyData.message.deserialize()
        ordersList = ordersData.message.deserialize()
    }

    override suspend fun reduce(state: LoginState, reduceAction: LoginReduceAction): LoginState {
        val newState = state.copy(
            loginStatus = LoginStatus.LOADED
        )
        Timber.tag(TAG).d("LoginStatus is ${newState.loginStatus}")
        return newState
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

    companion object {
        private val loginViewModel = LoginViewModel()
        private lateinit var cache: CacheData

        fun getInstance(): LoginViewModel = loginViewModel

        suspend fun getCache(): CacheData {
            with(loginViewModel) {
                return if (currencyChangeList.isEmpty() || ordersList.isEmpty()) {
                    getData()
                    cache = object : CacheData() {
                        override val currencyChanges = currencyChangeList
                        override val orders = ordersList
                    }
                    cache
                } else cache
            }
        }
    }
}


internal const val CURRENCIES_URL = "http://quiet-stone-2094.herokuapp.com/rates.json"
internal const val ORDERS_URL = "http://quiet-stone-2094.herokuapp.com/transactions.json"

enum class LoginError(val message: String) {
    NULL_REST_CALL_BODY("Null call request body"),
    BAD_REQUEST("Bad request, check connection or request URL")
}

internal const val TAG = "LOGIN"
