package me.ibrahim.profilemate.domain.base

import me.ibrahim.profilemate.data.api.NetworkResponse
import me.ibrahim.profilemate.domain.managers.ConnectionManager

open class BaseRepository {

    suspend fun <T : Any> withNetworkCheck(connectionManager: ConnectionManager, action: suspend () -> NetworkResponse<T>): NetworkResponse<T> {
        return if (connectionManager.isConnected()) {
            action.invoke()
        } else {
            NetworkResponse.Error(message = "Network Connection Error!")
        }
    }
}