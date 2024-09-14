package me.ibrahim.profilemate.data.remote

import android.util.Base64
import android.util.Patterns
import kotlinx.coroutines.flow.first
import me.ibrahim.profilemate.data.dto.LoginRequest
import me.ibrahim.profilemate.data.dto.LoginResponse
import me.ibrahim.profilemate.data.dto.UploadAvatarRequest
import me.ibrahim.profilemate.data.dto.UploadAvatarResponse
import me.ibrahim.profilemate.data.dto.UserProfileResponse
import me.ibrahim.profilemate.domain.managers.LocalDataStoreManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random


@Singleton
class ResponseBuilder @Inject constructor(
    val localDataStoreManager: LocalDataStoreManager
) {


    fun getLoginResponse(loginRequest: LoginRequest): Response<LoginResponse> {

        val validEmail = Patterns.EMAIL_ADDRESS.matcher(loginRequest.email).matches()

        return if (validEmail.not()) {
            val errorBody = "Invalid Email".toResponseBody("text/plain".toMediaTypeOrNull())
            Response.error(400, errorBody) //400 - Bad Request
        } else {
            val userId = generateRandomUserId()
            val token = generateToken()
            Response.success(LoginResponse(userid = userId, token = token))
        }
    }

    suspend fun getUserProfileResponse(userid: String): Response<UserProfileResponse> {
        return if (userid.isEmpty()) {
            val errorBody = "User Not Found".toResponseBody("text/plain".toMediaTypeOrNull())
            Response.error(404, errorBody)
        } else {

            val user = localDataStoreManager.readUser().first()

            val userProfileResponse = UserProfileResponse(
                avatarUrl = user?.avatarUrl ?: "",
                email = user?.email ?: ""
            )

            Response.success(userProfileResponse)
        }

    }

    fun getUploadAvatarResponse(uploadAvatarRequest: UploadAvatarRequest): Response<UploadAvatarResponse> {
        return if (uploadAvatarRequest.avatar.isEmpty()) {
            val errorBody = "Avatar Upload Failed".toResponseBody("text/plain".toMediaTypeOrNull())
            Response.error(404, errorBody)
        } else {
            //returning the url in the response.
            Response.success(UploadAvatarResponse(avatarUrl = uploadAvatarRequest.avatarUrl))
        }
    }


    private fun generateToken(): String {
        val expiryTime = System.currentTimeMillis() + (20 * 60 * 1000)//20 minutes
        val uniqueId = UUID.randomUUID().toString()
        val tokenData = "$expiryTime:$uniqueId"
        return Base64.encodeToString(tokenData.toByteArray(), Base64.NO_WRAP)
    }

    private fun generateRandomUserId(): String {
        val randomNumber = Random.nextInt(100, 1000)
        return "user-id-$randomNumber"
    }
}