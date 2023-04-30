package com.hoangnam.sharenote.ui.features.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Scaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hoangnam.sharenote.R
import com.hoangnam.sharenote.ui.components.RegisterAccountDialog
import com.hoangnam.sharenote.ui.components.SNButton
import com.hoangnam.sharenote.ui.components.SNTextField
import com.hoangnam.sharenote.ui.components.ShareNoteAppBar
import com.hoangnam.sharenote.ui.theme.ShareNoteTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: LoginViewModel?,
    state: LoginContract.State,
    effectFlow: Flow<LoginContract.Effect>?,
    onNavigationRequested: (username: String) -> Unit
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val showDialog =  remember { mutableStateOf(false) }

    if(showDialog.value)
        RegisterAccountDialog(setShowDialog = {
            showDialog.value = it
        }, onRegister = {
            viewModel!!.createUser(it)
        })

    LaunchedEffect(effectFlow) {
        effectFlow?.onEach { effect ->
            scope.launch {
                when (effect) {
                    is LoginContract.Effect.LoginSuccess -> {
                        onNavigationRequested(state.username)
                        snackbarHostState.showSnackbar(message = context.getString(R.string.msg_login_success), duration = SnackbarDuration.Short)
                    }

                    is LoginContract.Effect.LoginFail -> {
                        snackbarHostState.showSnackbar(message = context.getString(R.string.msg_login_fail) , duration = SnackbarDuration.Short)
                    }

                    is LoginContract.Effect.RegisterSuccess -> {
                        snackbarHostState.showSnackbar(message = context.getString(R.string.msg_register_success) , duration = SnackbarDuration.Short)
                    }

                    else -> {}
                }
            }
        }?.collect()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            ShareNoteAppBar(stringResource(id = R.string.app_name), false)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val username = remember { mutableStateOf(TextFieldValue()) }
            val password = remember { mutableStateOf(TextFieldValue()) }

            Spacer(modifier = Modifier.height(80.dp))
            SNTextField(textFieldValue = username ,label = stringResource(id = R.string.username), isError = state.usernameError, textError = stringResource(id = R.string.msg_username_empty)){
                viewModel!!.clearUsernameError()
            }
            Spacer(modifier = Modifier.height(20.dp))
            SNTextField(textFieldValue = password ,label = stringResource(id = R.string.password), isError = state.passwordError, textError = stringResource(id = R.string.msg_password_empty), isPassword = true){
                viewModel!!.clearPasswordError()
            }

            Spacer(modifier = Modifier.height(20.dp))
            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                SNButton(text = stringResource(id = R.string.login), isLoading = state.isLoading, isFillFullWidth = true){
                    viewModel!!.login(username.value.text, password.value.text)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            ClickableText(
                text = AnnotatedString(stringResource(id = R.string.register_account)),
                onClick = {
                    showDialog.value = true
                },
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ShareNoteTheme {
        LoginScreen(null ,LoginContract.State(), null, { })
    }
}