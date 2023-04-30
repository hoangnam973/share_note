package com.hoangnam.sharenote.ui.features.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoangnam.sharenote.data.repository.NoteRepositoryImpl
import com.hoangnam.sharenote.ui.NavigationKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val noteRepositoryImpl: NoteRepositoryImpl, private val stateHandle: SavedStateHandle,) :
    ViewModel() {

    var currentUser = ""

    var state by mutableStateOf(
        MainContract.State(
            isLoading = false,
            listOf()
        )
    )
        private set

    var effects = Channel<MainContract.Effect>(Channel.UNLIMITED)
        private set

    init {
        viewModelScope.launch {
            currentUser = stateHandle.get<String>(NavigationKeys.Arg.USERNAME)
                ?: throw IllegalStateException("No username was passed to destination.")
            getListNotes(username = currentUser)
        }
    }

    fun createNote(note: String){
        val currentTimestamp = System.currentTimeMillis()
        var note = com.hoangnam.sharenote.data.models.Note(content = note, user = currentUser, timeStamp = currentTimestamp.toString())
        viewModelScope.launch{
            state = state.copy(isLoading = true, listNotes = listOf())
            viewModelScope.launch {
                noteRepositoryImpl.createNote(note).collect {
                    when {
                        it.isSuccess -> {
                            effects.send(MainContract.Effect.CreateNoteSuccess)
                        }

                        it.isFailure -> {
                            it.exceptionOrNull()?.printStackTrace()
                        }
                    }
                }
            }
        }
    }

    fun changeListNotes(showAll: Boolean){
        var username = ""
        if(!showAll){
            username = currentUser
        }
        viewModelScope.launch {
            getListNotes(username = username)
        }
    }

    private suspend fun getListNotes(username: String) {
        state = state.copy(isLoading = true, listNotes = listOf())
        viewModelScope.launch {
            noteRepositoryImpl.getListNotes().collect {
                when {
                    it.isSuccess -> {
                        var listNotes = it.getOrNull()
                        if(username.isNotEmpty()){
                            listNotes = listNotes!!.filter { item -> item.user.equals(username) }
                            state = state.copy(isLoading = false, listNotes = listNotes!!.sortedByDescending { item -> item.timeStamp })
                        } else {
                            state = state.copy(isLoading = false, listNotes = listNotes!!.sortedByDescending { item -> item.timeStamp })
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