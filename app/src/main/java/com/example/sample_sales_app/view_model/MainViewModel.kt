package com.example.sample_sales_app.view_model

import androidx.lifecycle.viewModelScope
import com.example.sample_sales_app.data_model.CacheData
import com.example.sample_sales_app.data_model.Currency
import com.example.sample_sales_app.get_data.Cache
import com.example.sample_sales_app.utils.toastMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


object MainViewModel : BaseViewModel<MainActivityState, MainActivityUserIntent>() {
    private val reducer = MainReducer(MainActivityState.initial())

    override val state: StateFlow<MainActivityState> = reducer.state

    fun sendIntent(userIntent: MainActivityUserIntent) {
        viewModelScope.launch(Dispatchers.Main) {
            userIntent.setStateCache()
        }
        reducer.sendIntent(userIntent)
    }

    private class MainReducer(initialState: MainActivityState) :
        Reducer<MainActivityState, MainActivityUserIntent>(initialState) {
        override fun reduce(oldState: MainActivityState, userIntent: MainActivityUserIntent) {
            when (userIntent) {
                is MainActivityUserIntent.Login -> {
                    setState(
                        oldState.copy(
                            innerState = AppState.MAIN,
                            cache = state.value.cache
                        )
                    )
                }
            }
        }
    }
}

sealed class MainActivityUserIntent : UserIntent {
    object Login : MainActivityUserIntent()
    class SelectOrder(val code: String) : MainActivityUserIntent()
    class SelectCurrency(val currency: Currency) : MainActivityUserIntent()

    suspend fun setStateCache() {
        val oldCache = MainViewModel.state.value.cache
        MainViewModel.state.value.cache = Cache.get()
        if (oldCache != MainViewModel.state.value.cache)
            toastMessage("Data cached")
    }
}

data class MainActivityState(
    val innerState: AppState,
    var cache: CacheData,
    val mainScreenInfo: MainScreenInfo
) : UiState {
    companion object {
        fun initial() = MainActivityState(
            cache = CacheData(),
            innerState = AppState.LOGIN,
            mainScreenInfo = MainScreenInfo()
        )
    }
}

data class MainScreenInfo(
    val selectedCurrency: Currency? = null,
    val selectedOrder: String = ""
)

enum class AppState {
    LOGIN, MAIN
}