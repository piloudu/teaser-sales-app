package com.example.sample_sales_app

enum class LoadState {
    IDLE, LOADING, LOADED, ERROR
}

data class LoginState(
    val loadState: LoadState = LoadState.IDLE
) : State

object LoginIntent : MviIntent

object LoginAction : ReduceAction

class LoginViewModel : MviViewModel<LoginState, LoginIntent, LoginAction>(LoginState()) {
    override suspend fun executeIntent(mviIntent: LoginIntent) {
        handle(LoginAction)
    }

    override suspend fun reduce(state: LoginState, reduceAction: LoginAction): LoginState {
        // TODO
        return state
    }
}