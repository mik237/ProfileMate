package me.ibrahim.profilemate.data.managers

import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.ibrahim.profilemate.data.remote.NetworkResponse
import me.ibrahim.profilemate.domain.managers.ApiManager
import me.ibrahim.profilemate.domain.managers.ConnectionManager
import me.ibrahim.profilemate.domain.managers.LocalDataStoreManager
import me.ibrahim.profilemate.utils.fromJsonSafe
import retrofit2.HttpException
import retrofit2.Response
import java.net.UnknownHostException

class ApiManagerImpl(
    private val localDataStoreManager: LocalDataStoreManager,
    private val connectionManager: ConnectionManager,
    private val gson: Gson
) : ApiManager {

    /*suspend fun <T> newRequest(
        clazz: Class<T>,
        accessToken: String? = localDataStoreManager.getToken(),
        checkTokenExpiry: Boolean = true
    ): T {
//        checkAuthToken(accessToken, checkTokenExpiry)
        return retrofit.create(clazz)
    }*/


    override suspend fun <T : Any> handleApi(
        checkToken: Boolean,
        call: suspend () -> Response<T>
    ): NetworkResponse<T> {
        return try {
            if (connectionManager.isConnected().not()) {
                throw UnknownHostException()
            }

            if (checkToken) {
                val token = localDataStoreManager.getToken()
                // TODO: check for auth token first
            }

            val response = call.invoke()
            val body = response.body()
            val errorBody = response.errorBody()
            when {
                response.isSuccessful && body != null -> {
                    NetworkResponse.Success(body)
                }

                !response.isSuccessful && errorBody != null -> {
                    @Suppress("BlockingMethodInNonBlockingContext")
                    val error = withContext(Dispatchers.Main) {
                        errorBody.string()
                    }
                    val err = gson.fromJsonSafe(error, JsonObject::class.java)
                    val msg = err?.get("error")?.asJsonObject?.get("message")?.asString ?: ""
                    val code = err?.get("error")?.asJsonObject?.get("code")?.asInt
                    NetworkResponse.Error(message = msg, errorCode = code)
                }

                else -> {
                    NetworkResponse.Error(message = response.message())
                }
            }
        } catch (e: HttpException) {
            NetworkResponse.Error(message = e.message())
        } catch (e: Exception) {
            NetworkResponse.Error(message = e.localizedMessage)
        }
    }
}