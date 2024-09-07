package me.ibrahim.profilemate.domain.managers

import me.ibrahim.profilemate.data.remote.NetworkResponse
import retrofit2.Response

interface ApiManager {
    suspend fun <T : Any> handleApi(
        call: suspend () -> Response<T>
    ): NetworkResponse<T>
}