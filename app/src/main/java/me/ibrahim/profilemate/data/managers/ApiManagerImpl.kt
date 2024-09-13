package me.ibrahim.profilemate.data.managers

import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.ibrahim.profilemate.data.remote.NetworkResponse
import me.ibrahim.profilemate.domain.managers.ApiManager
import me.ibrahim.profilemate.utils.fromJsonSafe
import retrofit2.HttpException
import retrofit2.Response

class ApiManagerImpl(private val gson: Gson) : ApiManager {

    override suspend fun <T : Any> handleApi(
        call: suspend () -> Response<T>
    ): NetworkResponse<T> {
        return try {

            val response = call.invoke()
            val body = response.body()
            val errorBody = response.errorBody()
            when {
                response.isSuccessful && body != null -> {
                    NetworkResponse.Success(body)
                }

                !response.isSuccessful && errorBody != null -> {
                    val errorMsg = errorBody.string()
                    val code = response.code()
                    NetworkResponse.Error(errorMsg = errorMsg, errorCode = code)
                }

                else -> {
                    NetworkResponse.Error(errorMsg = response.message())
                }
            }
        } catch (e: HttpException) {
            NetworkResponse.Error(errorMsg = e.message())
        } catch (e: Exception) {
            NetworkResponse.Error(errorMsg = e.localizedMessage)
        }
    }
}