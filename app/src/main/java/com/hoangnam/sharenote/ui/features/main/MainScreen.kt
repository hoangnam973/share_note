package com.hoangnam.sharenote.ui.features.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hoangnam.sharenote.R
import com.hoangnam.sharenote.data.models.Note
import com.hoangnam.sharenote.ui.components.CreateNoteDialog
import com.hoangnam.sharenote.ui.components.ItemNote
import com.hoangnam.sharenote.ui.components.ShareNoteAppBar
import com.hoangnam.sharenote.ui.theme.Orange400
import com.hoangnam.sharenote.ui.theme.Purple40
import com.hoangnam.sharenote.ui.theme.ShareNoteTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel?,
    state: MainContract.State,
    effectFlow: Flow<MainContract.Effect>?,
    onNavigationRequested: () -> Unit
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val showDialog =  remember { mutableStateOf(false) }
    val showAllNotes =  remember { mutableStateOf(false) }

    if(showDialog.value)
        CreateNoteDialog(setShowDialog = {
            showDialog.value = it
        }, onCreate = {
            viewModel!!.createNote(it)
        })

    LaunchedEffect(effectFlow) {
        effectFlow?.onEach { effect ->
            scope.launch {
                when (effect) {
                    is MainContract.Effect.CreateNoteSuccess -> {
                        snackbarHostState.showSnackbar(message = context.getString(R.string.msg_create_note_success), duration = SnackbarDuration.Short)
                    }

                    else -> {}
                }
            }
        }?.collect()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            var title = if(showAllNotes.value){
                stringResource(id = R.string.all_notes)
            } else {
                stringResource(id = R.string.my_notes)
            }
            ShareNoteAppBar(title, true){
                onNavigationRequested()
            }
        },
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center,
            ) {
                FloatingActionButton(
                    containerColor = Orange400,
                    contentColor = Color.White,
                    onClick = {showDialog.value = true}
                ){
                    Icon(Icons.Filled.Add,"")
                }
                Spacer(modifier = Modifier.height(8.dp))
                FloatingActionButton(
                    containerColor = Purple40,
                    contentColor = Color.White,
                    onClick = {
                        showAllNotes.value = !showAllNotes.value
                        viewModel!!.changeListNotes(showAll = showAllNotes.value)
                    }
                ){
                    Icon(Icons.Filled.List, stringResource(id = R.string.change_list))
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                ListNotes(notes = state.listNotes)
            }
        }
    }
}

@Composable
fun ListNotes(
    notes: List<Note>
) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(notes) { item ->
            ItemNote(item = item)
        }
    }
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ShareNoteTheme {
        MainScreen(null ,MainContract.State(), null, { })
    }
}