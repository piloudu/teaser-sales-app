package com.example.sample_sales_app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

interface MviIntent

interface State

interface ReduceAction

abstract class MviViewModel<S : State, I : MviIntent, R : ReduceAction>(
    initialState: S,
) : ViewModel() {

    private val stateFlow = MutableStateFlow(initialState)
    val state = stateFlow.asStateFlow()
    private val intentFlow = MutableSharedFlow<I>()
    private val reduceFlow = MutableSharedFlow<R>()

    init {
        intentFlow
            .onEach { intent ->
                executeIntent(intent)
            }
            .launchIn(viewModelScope)
        reduceFlow
            .onEach { action ->
                stateFlow.value = reduce(stateFlow.value, action)
            }
            .launchIn(viewModelScope)
    }

    fun onIntent(mviIntent: I) {
        intentFlow.tryEmit(mviIntent)
    }

    fun handle(reduceAction: R) {
        reduceFlow.tryEmit(reduceAction)
    }
    
    protected abstract suspend fun executeIntent(mviIntent: I)

    protected abstract suspend fun reduce(state: S, reduceAction: R): S
}