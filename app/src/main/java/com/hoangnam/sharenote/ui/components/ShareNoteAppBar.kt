package com.hoangnam.sharenote.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareNoteAppBar(title: String, isShowLogout: Boolean, onItemClicked: () -> Unit = { }) {
    val color = if(isSystemInDarkTheme()){
        Color.White
    } else {
        Color.DarkGray
    }
    CenterAlignedTopAppBar(
        title = { Text(title , style = MaterialTheme.typography.titleMedium) },
                actions = {
                    if(isShowLogout) {
                        IconButton(onClick = onItemClicked) {
                            Icon(
                                imageVector = Icons.Filled.ExitToApp,
                                contentDescription = "Log out",
                                tint = color
                            )
                        }
                    }
        },
    )
}