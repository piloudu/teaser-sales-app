package com.example.sample_sales_app

import com.example.sample_sales_app.data_model.Currency.EUR
import com.example.sample_sales_app.view_model.AppState
import com.example.sample_sales_app.view_model.MainActivityState
import com.example.sample_sales_app.view_model.MainActivityUserIntent.*
import com.example.sample_sales_app.view_model.MainScreenInfo
import com.example.sample_sales_app.view_model.MainViewModel
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("The MVI machine")
class MviTest {
    private lateinit var MainViewModelTestInstance : MainViewModel
    
    @BeforeEach
    fun init() {
        MainViewModelTestInstance = object : MainViewModel() {}
    }

    @DisplayName("sets login state")
    @Test
    fun `is LoginUserIntent performed`() {
        withTestScope {
            val loggedState = MainActivityState.initial().copy(
                innerState = AppState.MAIN,
                cache = MainViewModelTestInstance.state.value.cache
            )
            MainViewModelTestInstance.sendIntent(Login)
            MainViewModelTestInstance.state.value shouldBeEqualToComparingFields loggedState
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
            MainViewModelTestInstance.sendIntent(SelectCurrency(EUR))
            MainViewModelTestInstance.state.value shouldBeEqualToComparingFields selectedCurrencyState
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
            MainViewModelTestInstance.sendIntent(SelectOrder("T2006"))
            MainViewModelTestInstance.state.value shouldBeEqualToComparingFields selectedOrderState
        }
    }
}