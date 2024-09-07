package me.ibrahim.profilemate.data.managers

import me.ibrahim.profilemate.domain.managers.LocalDataStoreManager
import me.ibrahim.profilemate.domain.managers.SessionManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManagerImpl @Inject constructor(val localDataStoreManager: LocalDataStoreManager) : SessionManager {

    override fun isSessionActive(): Boolean {
        return true
    }
}