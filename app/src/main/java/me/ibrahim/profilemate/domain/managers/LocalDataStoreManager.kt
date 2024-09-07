package me.ibrahim.profilemate.domain.managers

import kotlinx.coroutines.flow.Flow
import me.ibrahim.profilemate.domain.models.User

interface LocalDataStoreManager {

    suspend fun saveToken(token: String)
    suspend fun saveUser(user: User)

    fun readToken(): Flow<String>
    fun readUser(): Flow<User?>

    fun getToken(): String?

    suspend fun getUserId(): String
}