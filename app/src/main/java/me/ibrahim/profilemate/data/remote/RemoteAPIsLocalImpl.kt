package me.ibrahim.profilemate.data.remote

import me.ibrahim.profilemate.data.ResponseBuilder
import me.ibrahim.profilemate.data.dto.LoginRequest
import me.ibrahim.profilemate.data.dto.LoginResponse
import me.ibrahim.profilemate.data.dto.UploadPhotoRequest
import me.ibrahim.profilemate.data.dto.UploadPhotoResponse
import me.ibrahim.profilemate.data.dto.UserProfileResponse
import retrofit2.Response


/**
 * implementing this class to provide mock responses
 * in real code, this implementation is usually provided retrofit.
 */

class RemoteAPIsLocalImpl(val responseBuilder: ResponseBuilder) : RemoteAPIs {
    override suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> {
        return Response.success(responseBuilder.getLoginResponse())
    }

    override suspend fun getUserProfile(userid: String): Response<UserProfileResponse> {
        return Response.success(responseBuilder.getUserProfileResponse())
    }

    override suspend fun uploadProfileAvatar(userid: String, uploadPhotoRequest: UploadPhotoRequest): Response<UploadPhotoResponse> {
        return Response.success(responseBuilder.getUploadPhotoResponse())
    }
}