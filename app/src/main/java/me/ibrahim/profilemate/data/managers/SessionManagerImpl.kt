package me.ibrahim.profilemate.data.managers

import android.util.Base64
import me.ibrahim.profilemate.domain.managers.LocalDataStoreManager
import me.ibrahim.profilemate.domain.managers.SessionManager

class SessionManagerImpl(val localDataStoreManager: LocalDataStoreManager) : SessionManager {

    override suspend fun isActiveSession(): Boolean {
        val token = localDataStoreManager.getToken()
        token?.let {
            val decodedToken = String(Base64.decode(it, Base64.NO_WRAP))
            val tokenParts = decodedToken.split(":")
            val expiryTime = tokenParts[0].toLongOrNull() ?: return false
            val currentTime = System.currentTimeMillis()
            return currentTime < expiryTime
        } ?: return false
    }
}