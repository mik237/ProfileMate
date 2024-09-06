package me.ibrahim.profilemate.domain.repository

import kotlinx.coroutines.flow.Flow
import me.ibrahim.profilemate.data.remote.NetworkResponse
import me.ibrahim.profilemate.data.dto.LoginRequest
import me.ibrahim.profilemate.data.dto.LoginResponse

interface RemoteRepository {

    fun login(loginRequest: LoginRequest): Flow<NetworkResponse<LoginResponse>>
}