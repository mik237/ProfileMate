package me.ibrahim.profilemate.data.managers

import me.ibrahim.profilemate.domain.managers.LocalDataStoreManager
import me.ibrahim.profilemate.domain.managers.SessionManager

class SessionManagerImpl (val localDataStoreManager: LocalDataStoreManager) : SessionManager {

    override fun isActiveSession(): Boolean {
        return true
    }
}