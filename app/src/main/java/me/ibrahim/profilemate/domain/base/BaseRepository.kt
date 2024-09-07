package me.ibrahim.profilemate.domain.base

import me.ibrahim.profilemate.data.remote.NetworkResponse
import me.ibrahim.profilemate.domain.managers.ConnectionManager
import me.ibrahim.profilemate.domain.managers.SessionManager

open class BaseRepository {

    suspend fun <T> withNetworkCheck(connectionManager: ConnectionManager, action: suspend () -> NetworkResponse<T>): NetworkResponse<T> {
        return if (connectionManager.isConnected()) {
            action.invoke()
        } else {
            NetworkResponse.Error(errorMsg = "Network Connection Error!")
        }
    }

    suspend fun <T> withActiveSession(sessionManager: SessionManager, action: suspend () -> NetworkResponse<T>): NetworkResponse<T> {
        return if (sessionManager.isActiveSession()) {
            action.invoke()
        } else {
            NetworkResponse.Error(errorMsg = "Session Expired!")
        }
    }
}