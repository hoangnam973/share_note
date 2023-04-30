package com.hoangnam.sharenote.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.hoangnam.sharenote.R
import com.hoangnam.sharenote.data.models.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterAccountDialog(setShowDialog: (Boolean) -> Unit, onRegister: (User) -> Unit) {

    val username = remember { mutableStateOf(TextFieldValue()) }
    val password = remember { mutableStateOf(TextFieldValue()) }
    val usernameError = remember { mutableStateOf(false) }
    val passwordError = remember { mutableStateOf(false) }

    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Box() {
                Column(
                    Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
                ) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = stringResource(id = R.string.register_account),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    SNTextField(textFieldValue = username ,label = stringResource(id = R.string.username), isError = usernameError.value, textError = stringResource(id = R.string.msg_username_empty)){
                        usernameError.value = false
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    SNTextField(textFieldValue = password ,label = stringResource(id = R.string.password), isError = passwordError.value, textError = stringResource(id = R.string.msg_password_empty), isPassword = true){
                        passwordError.value = false
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Row() {
                        SNButton(text = stringResource(id = R.string.register)){
                                if(username.value.text.isEmpty()){
                                    usernameError.value = true
                                    return@SNButton
                                }
                                if(password.value.text.isEmpty()){
                                    passwordError.value = true
                                    return@SNButton
                                }
                                onRegister(User(username = username.value.text, password = password.value.text))
                                setShowDialog(false)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        SNButton(text = stringResource(id = R.string.cancel)){
                            setShowDialog(false)
                        }
                    }
                }
            }
        }
    }
}