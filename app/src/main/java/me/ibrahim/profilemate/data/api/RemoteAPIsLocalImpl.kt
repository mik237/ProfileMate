package me.ibrahim.profilemate.data.api

import me.ibrahim.profilemate.data.dto.LoginRequest
import me.ibrahim.profilemate.data.dto.LoginResponse
import me.ibrahim.profilemate.data.dto.UploadPhotoRequest
import me.ibrahim.profilemate.data.dto.UploadPhotoResponse
import me.ibrahim.profilemate.data.dto.UserProfileResponse
import retrofit2.Response


/**
 * implementing this class to provide mock responses
 * in real code, this implementation will be provided
 * retrofit.
 */

class RemoteAPIsLocalImpl : RemoteAPIs {
    override suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> {
        return Response.success(LoginResponse(userid = "123", token = "abcdef"))
    }

    override suspend fun getUserProfile(userid: String): Response<UserProfileResponse> {
        return Response.success(UserProfileResponse(avatarUrl = "", email = ""))
    }

    override suspend fun uploadProfileAvatar(userid: String, uploadPhotoRequest: UploadPhotoRequest): Response<UploadPhotoResponse> {
        return Response.success(UploadPhotoResponse(avatarUrl = ""))
    }
}