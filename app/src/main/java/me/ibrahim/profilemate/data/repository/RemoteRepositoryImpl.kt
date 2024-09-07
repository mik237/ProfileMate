package me.ibrahim.profilemate.data.repository

import android.net.Uri
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.ibrahim.profilemate.data.remote.NetworkResponse
import me.ibrahim.profilemate.data.remote.RemoteAPIs
import me.ibrahim.profilemate.data.dto.LoginRequest
import me.ibrahim.profilemate.data.dto.LoginResponse
import me.ibrahim.profilemate.data.dto.UploadAvatarRequest
import me.ibrahim.profilemate.data.dto.UploadAvatarResponse
import me.ibrahim.profilemate.data.dto.UserProfileResponse
import me.ibrahim.profilemate.domain.base.BaseRepository
import me.ibrahim.profilemate.domain.managers.ApiManager
import me.ibrahim.profilemate.domain.managers.ConnectionManager
import me.ibrahim.profilemate.domain.managers.SessionManager
import me.ibrahim.profilemate.domain.repository.RemoteRepository
import me.ibrahim.profilemate.utils.FileUtil

class RemoteRepositoryImpl(
    val remoteAPIs: RemoteAPIs,
    val apiManager: ApiManager,
    private val connectionManager: ConnectionManager,
    private val sessionManager: SessionManager,
    private val fileUtil: FileUtil
) : RemoteRepository, BaseRepository() {

    override fun login(loginRequest: LoginRequest): Flow<NetworkResponse<LoginResponse>> = flow {

        emit(NetworkResponse.Loading())

        //adding a delay to 500 milliseconds to show waiting of a network call
        delay(500L)

        val response = withNetworkCheck(connectionManager) { apiManager.handleApi { remoteAPIs.login(loginRequest) } }

        emit(response)
    }

    override fun getUser(userId: String): Flow<NetworkResponse<UserProfileResponse>> = flow {

        emit(NetworkResponse.Loading())

        //adding a delay to 500 milliseconds to show waiting of a network call
        delay(500L)

        val response = withNetworkCheck(connectionManager) { apiManager.handleApi { remoteAPIs.getUserProfile(userid = userId) } }

        emit(response)
    }

    override fun uploadAvatar(userId: String, uri: Uri): Flow<NetworkResponse<UploadAvatarResponse>> = flow {
        emit(NetworkResponse.Loading())
        //adding a delay to 500 milliseconds to show waiting of a network call
        delay(500L)

        val response = withNetworkCheck(connectionManager) {
            val bitmap = fileUtil.getBitmapFromUri(uri)
            val compressedFile = bitmap?.let { fileUtil.compressBitmap(it) }
            val compressedFileUri = compressedFile?.let { fileUtil.getFileUri(it) }
            val base64encodedBitmap = bitmap?.let { fileUtil.convertBitmapToBase64(it) } ?: ""

            val uploadAvatarRequest = UploadAvatarRequest(avatar = base64encodedBitmap, avatarUrl = compressedFileUri.toString())

            apiManager.handleApi { remoteAPIs.uploadProfileAvatar(userid = userId, uploadAvatarRequest = uploadAvatarRequest) }
        }

        emit(response)
    }
}