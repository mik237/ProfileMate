//package com.reasonlabs.familykeeper.data.remote
//
//import android.content.Context
//import androidx.core.net.toUri
//import com.google.gson.Gson
//import com.google.gson.JsonObject
//import com.logger.LogModule
//import com.logger.Logger
//import com.reasonlabs.familykeeper.core.BaseConsts
//import com.reasonlabs.familykeeper.data.repos.BaseTokenRepository
//
//import com.reasonlabs.familykeeper.data.responses.ApiResponse
//import com.reasonlabs.familykeeper.domain.models.ErrorResponse
//import com.reasonlabs.familykeeper.utils.BaseKeyHelper
//import com.reasonlabs.familykeeper.utils.ConnectionManager
//import com.reasonlabs.familykeeper.utils.Prefs
//import com.reasonlabs.familykeeper.utils.TokenUtil
//import com.reasonlabs.familykeeper.utils.extensions.fromJsonSafe
//import com.reasonlabs.familykeeper.utils.extensions.getMessageResValue
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//import okhttp3.*
//import okhttp3.MediaType.Companion.toMediaTypeOrNull
//import okhttp3.RequestBody.Companion.asRequestBody
//import okhttp3.RequestBody.Companion.toRequestBody
//import retrofit2.HttpException
//import retrofit2.Response
//import retrofit2.Retrofit
//import java.io.File
//import java.net.UnknownHostException
//import javax.inject.Inject
//import javax.inject.Singleton
//
//
//@Singleton
//class ApiManager @Inject constructor(
//    private val gson: Gson,
//    private val retrofit: Retrofit,
////    private val connectionManager: ConnectionManager,
//) {
//
//
//    suspend fun <T> newRequest(
//        clazz: Class<T>,
////        accessToken: String? = prefs.getPrefs(BaseConsts.PREFS_ACCESS_TOKEN),
//        checkTokenExpiry: Boolean = true
//    ): T {
//
////        checkAuthToken(accessToken, checkTokenExpiry)
//        return retrofit.create(clazz)
//    }
//
//    private suspend fun checkAuthToken(accessToken: String?, checkTokenExpiry: Boolean) {
//        if (checkTokenExpiry) {
//            getActiveAccessToken(accessToken)
//        }
//    }
//
//    private suspend fun getActiveAccessToken(token: String?): String? {
//        return when {
//            token == null ->  token
//            TokenUtil.isAccessTokenExpired(token) -> { tokenRepository.refreshAccessToken() }
//            else -> { token }
//        }
//    }
//
//    suspend fun <T : Any> handleApi(
//        call: suspend () -> Response<T>
//    ): ApiResponse<T> {
//        return try {
//            if(connectionManager.isConnected().not()){
//                throw UnknownHostException()
//            }
//            val response = call.invoke()
//            val body = response.body()
//            val errorBody = response.errorBody()
//            when {
//                response.isSuccessful && body != null -> {
//                    ApiResponse.Success(body)
//                }
//                !response.isSuccessful && errorBody != null -> {
//                    @Suppress("BlockingMethodInNonBlockingContext")
//                    val error = withContext(Dispatchers.Main) {
//                        errorBody.string()
//                    }
//                    val err = gson.fromJsonSafe(error, JsonObject::class.java)
//                    val msg = err?.get("error")?.asJsonObject?.get("message")?.asString ?: ""
//                    val code = err?.get("error")?.asJsonObject?.get("code")?.asInt
//                    ApiResponse.Failure(body = ErrorResponse(msg = msg, code = code))
//                }
//                else -> {
//                    ApiResponse.Failure(body = ErrorResponse(msg = response.message()))
//                }
//            }
//        } catch (e: HttpException) {
//            Logger.w(e, "Api HttpException", LogModule.API_CLIENT)
//            ApiResponse.Failure(body = ErrorResponse(msg = e.message()))
//        } catch (e: Exception) {
//            Logger.w(e, "Api Exception: ${e.printStackTrace()}", LogModule.API_CLIENT)
//            ApiResponse.Exception(e.getMessageResValue())
//        }
//    }
//    fun createPartFromString(content: String): RequestBody {
//        return content.toRequestBody(MultipartBody.FORM) // RequestBody.create(MultipartBody.FORM, content)
//    }
//
//    fun prepareFilePart(context: Context, partName: String, file: File): MultipartBody.Part {
//
//        // create RequestBody instance from file
//        var mediaType = ".jpg"
//        context.contentResolver.getType(file.toUri())?.let { mediaType = it }
//
//        val requestFile = file.asRequestBody(mediaType.toMediaTypeOrNull())
//
//        // MultipartBody.Part is used to send also the actual file name
//        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
//    }
//
//
//}