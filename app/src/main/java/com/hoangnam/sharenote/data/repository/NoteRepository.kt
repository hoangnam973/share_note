package com.hoangnam.sharenote.data.repository

import com.hoangnam.sharenote.data.models.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getListNotes() : Flow<Result<List<Note>>>
    fun createNote(note: Note) : Flow<Result<Boolean>>
}