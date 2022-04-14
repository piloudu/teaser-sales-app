package com.example.sample_sales_app.view_model

import kotlinx.coroutines.flow.*

abstract class Reducer<S : UiState, in I : UserIntent>(initialState: S) {

    private val _state: MutableStateFlow<S> = MutableStateFlow(initialState)
    val state = _state

    fun sendEvent(event: I) {
        reduce(_state.value, event)
    }

    fun setState(newState: S) {
        _state.tryEmit(newState)
    }

    abstract fun reduce(oldState: S, userIntent: I)
}

interface UserIntent

interface UiState