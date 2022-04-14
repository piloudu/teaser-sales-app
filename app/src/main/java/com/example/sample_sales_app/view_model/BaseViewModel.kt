package com.example.sample_sales_app.view_model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow

abstract class BaseViewModel<out S : UiState, in I : UserIntent> : ViewModel() {

    abstract val state: Flow<S>

}