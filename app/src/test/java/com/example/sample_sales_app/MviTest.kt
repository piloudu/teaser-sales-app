package com.example.sample_sales_app

import com.example.sample_sales_app.data_model.Currency.*
import com.example.sample_sales_app.view_model.*
import com.example.sample_sales_app.view_model.MainActivityUserIntent.*
import com.example.sample_sales_app.view_model.MainViewModel.state
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import org.junit.Rule
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("The MVI machine")
class MviTest {

    @AfterEach
    fun `reset state`() {
        MainViewModel.resetState()
    }

    @DisplayName("sets login state")
    @Test
    fun `is LoginUserIntent performed`() {
        withTestScope {
            val loggedState = MainActivityState.initial().copy(
                innerState = AppState.MAIN,
                cache = state.value.cache
            )
            MainViewModel.sendIntent(Login)
            state.value shouldBeEqualToComparingFields loggedState
        }
    }

    @DisplayName("sets currency state as selected")
    @Test
    fun `is the SelectCurrency user intent performed`() {
        withTestScope {
            val selectedCurrencyState = MainActivityState.initial().copy(
                mainScreenInfo = MainScreenInfo(
                    selectedCurrency = EUR
                )
            )
            MainViewModel.sendIntent(SelectCurrency(EUR))
            state.value shouldBeEqualToComparingFields selectedCurrencyState
        }
    }

    @DisplayName("sets order state as selected")
    @Test
    fun `is SelectOrder user intent performed`() {
        withTestScope {
            val selectedOrderState = MainActivityState.initial().copy(
                mainScreenInfo = MainScreenInfo(
                    selectedOrder = "T2006"
                )
            )
            MainViewModel.sendIntent(SelectOrder("T2006"))
            state.value shouldBeEqualToComparingFields selectedOrderState
        }
    }
}