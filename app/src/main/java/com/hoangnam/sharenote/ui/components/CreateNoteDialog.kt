package com.hoangnam.sharenote.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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

@Composable
fun CreateNoteDialog(setShowDialog: (Boolean) -> Unit, onCreate: (String) -> Unit) {

    val note = remember { mutableStateOf(TextFieldValue()) }
    val noteError = remember { mutableStateOf(false) }

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
                        text = stringResource(id = R.string.create_note),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    SNTextField(textFieldValue = note ,label = stringResource(id = R.string.your_note), isError = noteError.value, textError = stringResource(id = R.string.msg_note_empty)){
                        noteError.value = false
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Row() {
                        SNButton(text = stringResource(id = R.string.create)){
                                                            if(note.value.text.isEmpty()){
                                    noteError.value = true
                                    return@SNButton
                                }
                                onCreate(note.value.text)
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