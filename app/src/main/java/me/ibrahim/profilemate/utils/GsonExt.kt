package me.ibrahim.profilemate.utils

import com.google.gson.Gson

fun <T> Gson.fromJsonSafe(jsonStr: String, classType: Class<T>): T? {
    return try {
        this.fromJson(jsonStr, classType)
    } catch (e: Exception) {
        null
    }
}

fun <T> Gson.toJsonSafe(classType: T): String {
    return try {
        this.toJson(classType)
    } catch (e: Exception) {
        ""
    }
}