package com.example.sample_sales_app.view_model

import androidx.lifecycle.viewModelScope
import com.example.sample_sales_app.data_model.CacheData
import com.example.sample_sales_app.data_model.CurrencyChange
import com.example.sample_sales_app.data_model.Order
import com.example.sample_sales_app.get_data.Cache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


object MainViewModel : BaseViewModel<MainActivityState, MainActivityUserIntent>() {
    private val reducer = MainReducer(MainActivityState.initial())

    override val state: StateFlow<MainActivityState> = reducer.state

    fun sendIntent(userIntent: MainActivityUserIntent) {
        reducer.sendIntent(userIntent)
    }

    private class MainReducer(initialState: MainActivityState) :
        Reducer<MainActivityState, MainActivityUserIntent>(initialState) {
        override fun reduce(oldState: MainActivityState, userIntent: MainActivityUserIntent) {
            withMainViewModelScope {
                when (userIntent) {
                    is MainActivityUserIntent.Login -> {
                        val cache = Cache.get()
                        setState(
                            oldState.copy(
                                cache = cache
                            )
                        )
                    }
                }
            }
        }
    }

    private fun withMainViewModelScope(scope: suspend CoroutineScope.() -> Unit) {
        val dispatcher = Dispatchers.Default
        viewModelScope.launch(dispatcher) { scope() }
    }
}

sealed class MainActivityUserIntent : UserIntent {
    object Login : MainActivityUserIntent()
}

data class MainActivityState(
    val isLoading: Boolean,
    val cache: CacheData,
) : UiState {
    companion object {
        private val initialCache = object : CacheData() {
            override val currencyChanges = emptyList<CurrencyChange>()
            override val orders = emptyList<Order>()
        }

        fun initial() = MainActivityState(
            isLoading = true,
            cache = initialCache
        )
    }
}
