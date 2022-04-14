package com.example.sample_sales_app.view_model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow

abstract class BaseViewModel<out S : UiState, in E : UiEvent> : ViewModel() {

    abstract val state: Flow<S>

}