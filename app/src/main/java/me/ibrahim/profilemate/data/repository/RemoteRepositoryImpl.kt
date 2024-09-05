package me.ibrahim.profilemate.data.repository

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.ibrahim.profilemate.data.api.NetworkResponse
import me.ibrahim.profilemate.data.api.RemoteAPIs
import me.ibrahim.profilemate.data.dto.LoginRequest
import me.ibrahim.profilemate.data.dto.LoginResponse
import me.ibrahim.profilemate.domain.base.BaseRepository
import me.ibrahim.profilemate.domain.managers.ApiManager
import me.ibrahim.profilemate.domain.repository.RemoteRepository

class RemoteRepositoryImpl(
    val remoteAPIs: RemoteAPIs,
    val apiManager: ApiManager
) : RemoteRepository, BaseRepository() {

    override fun login(loginRequest: LoginRequest): Flow<NetworkResponse<LoginResponse>> = flow {


        emit(NetworkResponse.Loading())

        //adding a delay to 500 milliseconds to show waiting of a network call
        delay(500L)

        val response = apiManager.handleApi { remoteAPIs.login(loginRequest) }

        emit(response)
    }
}