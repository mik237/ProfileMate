package me.ibrahim.profilemate.data.api

import me.ibrahim.profilemate.data.dto.LoginRequest
import me.ibrahim.profilemate.data.dto.LoginResponse
import me.ibrahim.profilemate.data.dto.UploadPhotoRequest
import me.ibrahim.profilemate.data.dto.UploadPhotoResponse
import me.ibrahim.profilemate.data.dto.UserProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RemoteAPIs {

    @POST("/sessions/new")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>


    @GET("/users/{userid}")
    suspend fun getUserProfile(@Path("userid") userid: String): Response<UserProfileResponse>

    @POST("/users/{userid}/avatar")
    suspend fun uploadProfileAvatar(
        @Path("userid") userid: String,
        @Body uploadPhotoRequest: UploadPhotoRequest
    ): Response<UploadPhotoResponse>
}