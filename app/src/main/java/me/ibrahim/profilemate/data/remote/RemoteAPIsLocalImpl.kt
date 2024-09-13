package me.ibrahim.profilemate.data.remote

import me.ibrahim.profilemate.data.dto.LoginRequest
import me.ibrahim.profilemate.data.dto.LoginResponse
import me.ibrahim.profilemate.data.dto.UploadAvatarRequest
import me.ibrahim.profilemate.data.dto.UploadAvatarResponse
import me.ibrahim.profilemate.data.dto.UserProfileResponse
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import kotlin.math.log


/**
 * implementing this class to provide mock responses
 * in real code, this implementation is usually provided retrofit.
 */

class RemoteAPIsLocalImpl(val responseBuilder: ResponseBuilder) : RemoteAPIs {

    override suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> {
        return responseBuilder.getLoginResponse(loginRequest)
    }

    override suspend fun getUserProfile(userid: String): Response<UserProfileResponse> {
        return responseBuilder.getUserProfileResponse(userid = userid)
    }

    override suspend fun uploadProfileAvatar(userid: String, uploadAvatarRequest: UploadAvatarRequest): Response<UploadAvatarResponse> {
        return Response.success(responseBuilder.getUploadAvatarResponse(uploadAvatarRequest))
    }
}