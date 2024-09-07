package me.ibrahim.profilemate.data.remote


sealed class NetworkResponse<T> {
    data class Success<T>(val data: T) : NetworkResponse<T>()
    data class Error<T>(val errorMsg: String? = null, val errorCode: Int? = null) : NetworkResponse<T>()
    class Loading<T> : NetworkResponse<T>()
}