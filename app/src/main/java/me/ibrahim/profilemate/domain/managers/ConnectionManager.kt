package me.ibrahim.profilemate.domain.managers

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import javax.inject.Inject

class ConnectionManager @Inject constructor(val app: Application) {

    fun isConnected(): Boolean {
        return isWifiEnabled() || isCellularDataEnabled()
    }

    fun isWifiEnabled(): Boolean {
        return isEnabled(NetworkCapabilities.TRANSPORT_WIFI)
    }

    fun isCellularDataEnabled(): Boolean {
        return isEnabled(NetworkCapabilities.TRANSPORT_CELLULAR)
    }

    private fun isEnabled(transportType : Int) : Boolean {
        val connectivityManager = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return activeNetwork.hasTransport(transportType)
    }
}