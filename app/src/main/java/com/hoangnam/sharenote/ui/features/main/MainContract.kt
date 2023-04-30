package com.hoangnam.sharenote.ui.features.main

import com.hoangnam.sharenote.data.models.Note

class MainContract {
    data class State(
        val isLoading: Boolean = false,
        val listNotes: List<Note> = listOf()
    )

    sealed class Effect {
        object CreateNoteSuccess : MainContract.Effect()
    }
}