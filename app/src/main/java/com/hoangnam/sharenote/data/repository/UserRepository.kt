package com.hoangnam.sharenote.data.repository

import com.hoangnam.sharenote.data.models.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getListUsers() : Flow<Result<List<User>>>
    fun createUser(user: User) : Flow<Result<Boolean>>
}