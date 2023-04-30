package com.hoangnam.sharenote.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.hoangnam.sharenote.data.models.Note
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class NoteRepositoryImpl @Inject constructor(private val databaseReference: DatabaseReference) : NoteRepository{
    override fun getListNotes() = callbackFlow<Result<List<Note>>> {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var listNotes = mutableListOf<Note>()
                for (noteSnapshot in dataSnapshot.children) {
                    var note = noteSnapshot.getValue(Note::class.java)
                    listNotes.add(note!!)
                }
                this@callbackFlow.trySendBlocking(Result.success(listNotes))
            }

            override fun onCancelled(databaseError: DatabaseError) {
                this@callbackFlow.trySendBlocking(Result.failure(databaseError.toException()))
            }
        }
        databaseReference.child(TABLE_NOTES).addValueEventListener(postListener)

        awaitClose {
            databaseReference.child(TABLE_NOTES)
                .removeEventListener(postListener)
        }
    }

    override fun createNote(note: Note) = callbackFlow<Result<Boolean>> {
        try {
            val randomId = Random.nextInt(0, 10000)
            databaseReference.child(TABLE_NOTES).child(randomId.toString()).setValue(note)
            this@callbackFlow.trySendBlocking(Result.success(true))
        } catch (e: Exception){
            this@callbackFlow.trySendBlocking(Result.failure(e))
        }

        awaitClose {
            this.close()
        }
    }

    companion object {
        const val TABLE_NOTES = "notes"
    }
}