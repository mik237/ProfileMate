package me.ibrahim.profilemate.domain.use_cases.profile

import kotlinx.coroutines.flow.Flow
import me.ibrahim.profilemate.data.dto.UserProfileResponse
import me.ibrahim.profilemate.data.remote.NetworkResponse
import me.ibrahim.profilemate.domain.managers.LocalDataStoreManager
import me.ibrahim.profilemate.domain.repository.RemoteRepository
import javax.inject.Inject


class ReadRemoteUserUseCase @Inject constructor(
    val localDataStoreManager: LocalDataStoreManager,
    private val remoteRepository: RemoteRepository
) {
    suspend operator fun invoke(): Flow<NetworkResponse<UserProfileResponse>> {
        val userId = localDataStoreManager.getUserId()
        return remoteRepository.getUser(userId)
    }
}