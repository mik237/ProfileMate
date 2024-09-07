package me.ibrahim.profilemate.domain.use_cases.profile

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import me.ibrahim.profilemate.data.dto.UploadAvatarRequest
import me.ibrahim.profilemate.data.dto.UploadAvatarResponse
import me.ibrahim.profilemate.data.remote.NetworkResponse
import me.ibrahim.profilemate.domain.managers.LocalDataStoreManager
import me.ibrahim.profilemate.domain.repository.RemoteRepository
import me.ibrahim.profilemate.utils.FileUtil
import javax.inject.Inject


class UploadAvatarUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository,
    val localDataStoreManager: LocalDataStoreManager
) {

    suspend operator fun invoke(uri: Uri): Flow<NetworkResponse<UploadAvatarResponse>> {
        val userId = localDataStoreManager.getUserId()
        return remoteRepository.uploadAvatar(userId = userId, uri = uri)
    }

}