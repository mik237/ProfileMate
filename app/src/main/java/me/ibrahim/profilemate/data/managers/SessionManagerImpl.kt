package me.ibrahim.profilemate.data.managers

import me.ibrahim.profilemate.domain.managers.LocalDataStoreManager
import me.ibrahim.profilemate.domain.managers.SessionManager
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class SessionManagerImpl(val localDataStoreManager: LocalDataStoreManager) : SessionManager {

    @OptIn(ExperimentalEncodingApi::class)
    override fun isActiveSession(): Boolean {
        val token = localDataStoreManager.getToken()
        token?.let {
            val decodedToken = String(Base64.decode(it))
            val tokenParts = decodedToken.split(":")
            val expiryTime = tokenParts[0].toLongOrNull() ?: return false
            val currentTime = System.currentTimeMillis()
            return currentTime < expiryTime
        } ?: return false
    }
}