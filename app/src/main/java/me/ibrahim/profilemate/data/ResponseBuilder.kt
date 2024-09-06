package me.ibrahim.profilemate.data

import kotlinx.coroutines.flow.first
import me.ibrahim.profilemate.data.dto.LoginResponse
import me.ibrahim.profilemate.data.dto.UploadPhotoResponse
import me.ibrahim.profilemate.data.dto.UserProfileResponse
import me.ibrahim.profilemate.domain.managers.LocalDataStoreManager
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.random.Random


@Singleton
class ResponseBuilder @Inject constructor(
    val localDataStoreManager: LocalDataStoreManager
) {


    fun getLoginResponse(): LoginResponse {
        val userId = generateRandomUserId()
        val token = generateToken()
        return LoginResponse(userid = userId, token = token)
    }

    suspend fun getUserProfileResponse(): UserProfileResponse {
        val user = localDataStoreManager.readUser().first()
        return UserProfileResponse(avatarUrl = user?.avatarUrl ?: "", email = user?.email ?: "")
    }

    suspend fun getUploadPhotoResponse(): UploadPhotoResponse {
        val user = localDataStoreManager.readUser().first()
        return UploadPhotoResponse(avatarUrl = user?.avatarUrl ?: "")
    }


    @OptIn(ExperimentalEncodingApi::class)
    private fun generateToken(): String {
        val expiryTime = System.currentTimeMillis() + (10 * 60 * 1000)
        val uniqueId = UUID.randomUUID().toString()
        val tokenData = "$expiryTime:$uniqueId"
        return Base64.encode(tokenData.toByteArray())
    }

    private fun generateRandomUserId(): String {
        val randomNumber = Random.nextInt(100, 1000)
        return "user-id-$randomNumber"
    }
}