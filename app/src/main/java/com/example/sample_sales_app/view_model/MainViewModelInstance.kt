package com.example.sample_sales_app.view_model

import androidx.lifecycle.viewModelScope
import com.example.sample_sales_app.data_model.CacheData
import com.example.sample_sales_app.data_model.Currency
import com.example.sample_sales_app.get_data.Cache
import com.example.sample_sales_app.get_data.TotalAmount
import com.example.sample_sales_app.utils.toastMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * MainViewModel is not made a singleton in order for tests to be able to instance a new object
 * for each case
 */
abstract class MainViewModel : BaseViewModel<MainActivityState, MainActivityUserIntent>() {
    private val reducer = MainReducer(MainActivityState.initial())

    override val state: StateFlow<MainActivityState> = reducer.state

    fun sendIntent(userIntent: MainActivityUserIntent) {
        viewModelScope.launch(Dispatchers.Main) {
            userIntent.setStateCache()
            userIntent.action()
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
                is MainActivityUserIntent.SelectCurrency -> {
                    setState(
                        oldState.copy(
                            mainScreenInfo = oldState.mainScreenInfo.copy(
                                selectedCurrency = userIntent.currency
                            )
                        )
                    )
                }
                is MainActivityUserIntent.SelectOrder -> {
                    setState(
                        oldState.copy(
                            mainScreenInfo = oldState.mainScreenInfo.copy(
                                selectedOrder = userIntent.orderCode,
                            )
                        )
                    )
                }
            }
        }
    }
}

/**
 * Create a MainViewModel instance to be used in the whole app
 */
object MainViewModelInstance : MainViewModel()

sealed class MainActivityUserIntent : UserIntent {
    object Login : MainActivityUserIntent()
    class SelectOrder(val orderCode: String) : MainActivityUserIntent() {
        override suspend fun action() = TotalAmount.getFor(
            orderCode = orderCode,
            cache = Cache.get()
        )
    }


    class SelectCurrency(val currency: Currency) : MainActivityUserIntent() {
        override suspend fun action() = TotalAmount.getFor(
            targetCurrency = currency,
            cache = Cache.get()
        )
    }

    suspend fun setStateCache() {
        val oldCache = MainViewModelInstance.state.value.cache
        MainViewModelInstance.state.value.cache = Cache.get()
        if (oldCache != MainViewModelInstance.state.value.cache)
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
    val selectedOrder: String? = null
)

enum class AppState {
    LOGIN, MAIN
}