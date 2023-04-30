package com.hoangnam.sharenote.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hoangnam.sharenote.R
import com.hoangnam.sharenote.data.models.Note
import com.hoangnam.sharenote.ui.theme.ShareNoteTheme
import com.hoangnam.sharenote.ui.theme.Yellow50
import com.hoangnam.sharenote.utils.DateUtils

@Composable
fun ItemNote(
    item: Note
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = Yellow50,
        elevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp),
        ) {
            item.user?.let {
                Row (verticalAlignment = Alignment.Bottom){
                    Text(
                        text = stringResource(id = R.string.created_by),
                        style = MaterialTheme.typography.labelSmall
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
            item.content?.let {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = it,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            item.timeStamp?.let {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = DateUtils.convertTimestampToString(it)!!,
                    style = MaterialTheme.typography.labelSmall.copy(fontStyle = FontStyle.Italic),
                    textAlign = TextAlign.Right
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ShareNoteTheme {
        ItemNote(Note(content = "hello world", user = "hoangnam", "1682828141737"))
    }
}