package me.ibrahim.profilemate.data.remote

import kotlinx.coroutines.runBlocking
import me.ibrahim.profilemate.domain.managers.LocalDataStoreManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthInterceptor(private val localDataStoreManager: LocalDataStoreManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()
        val token = runBlocking { localDataStoreManager.getToken() }

        // Add the Authorization header
        val newRequest: Request = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        return chain.proceed(newRequest)
    }
}