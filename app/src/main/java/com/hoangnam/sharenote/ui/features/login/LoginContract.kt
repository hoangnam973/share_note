package com.hoangnam.sharenote.ui.features.login

class LoginContract {
    data class State(
        val isLoading: Boolean = false,
        val username: String = "",
        val usernameError: Boolean = false,
        val passwordError: Boolean = false,
    )

    sealed class Effect {
        object LoginSuccess : Effect()
        object LoginFail : Effect()
        object RegisterSuccess : Effect()
    }
}