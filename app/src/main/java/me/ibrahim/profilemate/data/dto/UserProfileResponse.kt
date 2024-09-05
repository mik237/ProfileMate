package me.ibrahim.profilemate.data.dto

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class UserProfileResponse(
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("email")
    val email: String
) : Parcelable