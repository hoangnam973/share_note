package com.hoangnam.sharenote.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hoangnam.sharenote.utils.conditional

@Composable
fun SNButton(
    text: String,
    isLoading: Boolean = false,
    isFillFullWidth: Boolean = false,
    onClick: () -> Unit = { },
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .conditional(isFillFullWidth){
                fillMaxWidth()
            }
            .height(50.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Text(text = text)
        }
    }
}