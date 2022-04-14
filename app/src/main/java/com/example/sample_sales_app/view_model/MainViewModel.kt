/*
 * Copyright (c) 14/4/22 by Ancora. All rights reserved.
 * This code cannot be used, copied, modified and/or distributed without
 * the express permission of the authors.
 *
 * Created by xoan on 14/4/22
 */

package com.example.sample_sales_app.view_model

import com.example.sample_sales_app.data_model.CacheData
import com.example.sample_sales_app.data_model.CurrencyChange
import com.example.sample_sales_app.data_model.Order
import kotlinx.coroutines.flow.StateFlow


class MainViewModel : BaseViewModel<MainActivityState, MainActivityUserIntent>() {

    private val reducer = MainReducer(MainActivityState.initial())

    override val state: StateFlow<MainActivityState> = reducer.state

    fun sendIntent(userIntent: MainActivityUserIntent) {
        reducer.sendIntent(userIntent)
    }

    private class MainReducer(initialState: MainActivityState) :
        Reducer<MainActivityState, MainActivityUserIntent>(initialState) {
        override fun reduce(oldState: MainActivityState, userIntent: MainActivityUserIntent) {
            when (userIntent) {
                is MainActivityUserIntent.Login -> setState(
                    oldState.copy(

                    )
                )
            }
        }
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
