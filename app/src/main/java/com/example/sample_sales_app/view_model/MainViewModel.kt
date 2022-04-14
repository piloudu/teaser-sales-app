package com.example.sample_sales_app.view_model

import android.widget.Toast
import androidx.lifecycle.viewModelScope
import com.example.sample_sales_app.MainActivity
import com.example.sample_sales_app.data_model.CacheData
import com.example.sample_sales_app.get_data.Cache
import com.example.sample_sales_app.utils.toastMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


object MainViewModel : BaseViewModel<MainActivityState, MainActivityUserIntent>() {
    private val reducer = MainReducer(MainActivityState.initial())

    override val state: StateFlow<MainActivityState> = reducer.state

    fun sendIntent(userIntent: MainActivityUserIntent) {
        reducer.sendIntent(userIntent)
    }

    private class MainReducer(initialState: MainActivityState) :
        Reducer<MainActivityState, MainActivityUserIntent>(initialState) {
        override fun reduce(oldState: MainActivityState, userIntent: MainActivityUserIntent) {
            when (userIntent) {
                is MainActivityUserIntent.Login -> {
                    cacheData()
                    setState(
                        oldState.copy(
                            innerState = AppState.LOGIN,
                            cache = state.value.cache
                        )
                    )
                }
            }
        }
    }

    private fun cacheData() {
        viewModelScope.launch(Dispatchers.Main) {
            state.value.cache = Cache.get()
            toastMessage("Data cached")
        }
    }
}

sealed class MainActivityUserIntent : UserIntent {
    object Login : MainActivityUserIntent()
}

data class MainActivityState(
    val innerState: AppState,
    val isLoading: Boolean,
    var cache: CacheData,
) : UiState {
    companion object {
        fun initial() = MainActivityState(
            isLoading = true,
            cache = CacheData(),
            innerState = AppState.IDLE
        )
    }
}

enum class AppState {
    IDLE, LOGIN
}