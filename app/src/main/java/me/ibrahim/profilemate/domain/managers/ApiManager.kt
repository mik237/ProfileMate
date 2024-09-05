package me.ibrahim.profilemate.domain.managers

import me.ibrahim.profilemate.data.api.NetworkResponse
import retrofit2.Response

interface ApiManager {
    suspend fun <T : Any> handleApi(
        checkToken: Boolean = true,
        call: suspend () -> Response<T>
    ): NetworkResponse<T>
}