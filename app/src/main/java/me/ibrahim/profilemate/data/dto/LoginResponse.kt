package me.ibrahim.profilemate.data.dto

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class LoginResponse(val userid: String, val token: String) : Parcelable
