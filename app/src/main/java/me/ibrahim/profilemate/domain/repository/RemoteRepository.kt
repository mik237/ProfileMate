package me.ibrahim.profilemate.domain.repository

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import me.ibrahim.profilemate.data.remote.NetworkResponse
import me.ibrahim.profilemate.data.dto.LoginRequest
import me.ibrahim.profilemate.data.dto.LoginResponse
import me.ibrahim.profilemate.data.dto.UploadAvatarRequest
import me.ibrahim.profilemate.data.dto.UploadAvatarResponse
import me.ibrahim.profilemate.data.dto.UserProfileResponse

interface RemoteRepository {

    fun login(loginRequest: LoginRequest): Flow<NetworkResponse<LoginResponse>>

    fun getUser(userId: String): Flow<NetworkResponse<UserProfileResponse>>

    fun uploadAvatar(userId: String, uri: Uri): Flow<NetworkResponse<UploadAvatarResponse>>
}