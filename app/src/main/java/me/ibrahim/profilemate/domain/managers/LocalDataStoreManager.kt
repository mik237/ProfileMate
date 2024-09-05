package me.ibrahim.profilemate.domain.managers

import kotlinx.coroutines.flow.Flow

interface LocalDataStoreManager {

    suspend fun saveToken(token: String)

    fun readToken(): Flow<String>

    fun getToken(): String?
}