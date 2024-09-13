package me.ibrahim.profilemate.domain.managers

interface SessionManager {
    suspend fun isActiveSession(): Boolean
}