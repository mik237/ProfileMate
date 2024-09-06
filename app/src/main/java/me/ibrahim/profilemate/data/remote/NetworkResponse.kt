package me.ibrahim.profilemate.data.remote


sealed class NetworkResponse<T> {
    data class Success<T>(val data: T) : NetworkResponse<T>()
    data class Error<T>(val message: String? = null, val errorCode: Int? = null) : NetworkResponse<T>()
    class Loading<T> : NetworkResponse<T>()
}