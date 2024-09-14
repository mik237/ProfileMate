package me.ibrahim.profilemate.data.managers

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import me.ibrahim.profilemate.domain.managers.ConnectionManager

class ConnectionManagerImpl(val app: Application) : ConnectionManager {

    override fun isConnected(): Boolean {
        return isWifiEnabled() || isCellularDataEnabled()
    }

    private fun isWifiEnabled(): Boolean {
        return isEnabled(NetworkCapabilities.TRANSPORT_WIFI)
    }

    private fun isCellularDataEnabled(): Boolean {
        return isEnabled(NetworkCapabilities.TRANSPORT_CELLULAR)
    }

    private fun isEnabled(transportType: Int): Boolean {
        val connectivityManager = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return activeNetwork.hasTransport(transportType)
    }
}