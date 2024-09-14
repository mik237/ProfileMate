package me.ibrahim.profilemate.domain.managers

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import javax.inject.Inject

interface ConnectionManager {
    fun isConnected(): Boolean
}