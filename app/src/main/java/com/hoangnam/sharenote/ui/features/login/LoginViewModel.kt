package com.hoangnam.sharenote.ui.features.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoangnam.sharenote.data.models.User
import com.hoangnam.sharenote.data.repository.UserRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userRepositoryImpl: UserRepositoryImpl) :
    ViewModel() {

    var state by mutableStateOf(
        LoginContract.State(
            isLoading = false
        )
    )
        private set

    var effects = Channel<LoginContract.Effect>(Channel.UNLIMITED)
        private set

    fun createUser(user: User){
        viewModelScope.launch {
            userRepositoryImpl.createUser(user).collect {
                when {
                    it.isSuccess -> {
                        effects.send(LoginContract.Effect.RegisterSuccess)
                    }

                    it.isFailure -> {
                        it.exceptionOrNull()?.printStackTrace()
                    }
                }
            }
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            checkUser(username, password)
        }
    }

    fun clearUsernameError(){
        state = state.copy(usernameError = false)
    }

    fun clearPasswordError(){
        state = state.copy(passwordError = false)
    }

    private suspend fun checkUser(username: String, password: String) {
        if(username.isEmpty()){
            state = state.copy(usernameError = true)
            return
        }
        if(password.isEmpty()){
            state = state.copy(passwordError = true)
            return
        }
        viewModelScope.launch {
            state = state.copy(isLoading = true, username = username)
            userRepositoryImpl.getListUsers().collect {
                when {
                    it.isSuccess -> {
                        state = state.copy(isLoading = false, username = username)
                        val listUsers = it.getOrNull()
                        if(listUsers != null){
                            var canLogin = false
                            for(user in listUsers){
                                if(user.username.equals(username) && user.password.equals(password)){
                                    canLogin = true
                                    break
                                }
                            }
                            if(canLogin){
                                effects.send(LoginContract.Effect.LoginSuccess)
                            } else {
                                effects.send(LoginContract.Effect.LoginFail)
                            }

                        }
                    }
                    it.isFailure -> {
                        it.exceptionOrNull()?.printStackTrace()
                    }
                }
            }
        }
    }
}
