package com.hoangnam.sharenote.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.hoangnam.sharenote.data.models.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random


@Singleton
class UserRepositoryImpl @Inject constructor(private val databaseReference: DatabaseReference) : UserRepository{
    override fun getListUsers() = callbackFlow<Result<List<User>>> {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var listUsers = mutableListOf<User>()
                for (userSnapshot in dataSnapshot.children) {
                    var user = userSnapshot.getValue(User::class.java)
                    listUsers.add(user!!)
                }
                this@callbackFlow.trySendBlocking(Result.success(listUsers))
            }

            override fun onCancelled(databaseError: DatabaseError) {
                this@callbackFlow.trySendBlocking(Result.failure(databaseError.toException()))
            }
        }
        databaseReference.child(TABLE_USERS).addValueEventListener(postListener)

        awaitClose {
            databaseReference.child(TABLE_USERS)
                .removeEventListener(postListener)
        }
    }

    override fun createUser(user: User) = callbackFlow<Result<Boolean>> {
        try {
            val randomId = Random.nextInt(0, 10000)
            databaseReference.child(UserRepositoryImpl.TABLE_USERS).child(randomId.toString()).setValue(user)
            this@callbackFlow.trySendBlocking(Result.success(true))
        } catch (e: Exception){
            this@callbackFlow.trySendBlocking(Result.failure(e))
        }

        awaitClose {
            this.close()
        }
    }

    companion object {
        const val TABLE_USERS = "users"
    }
}