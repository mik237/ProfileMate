package me.ibrahim.profilemate.domain.use_cases

import kotlinx.coroutines.flow.Flow
import me.ibrahim.profilemate.data.remote.NetworkResponse
import me.ibrahim.profilemate.data.dto.LoginRequest
import me.ibrahim.profilemate.data.dto.LoginResponse
import me.ibrahim.profilemate.domain.repository.RemoteRepository

class LoginUseCase(private val remoteRepository: RemoteRepository) {
    operator fun invoke(loginRequest: LoginRequest): Flow<NetworkResponse<LoginResponse>> {
        return remoteRepository.login(loginRequest)
    }
}