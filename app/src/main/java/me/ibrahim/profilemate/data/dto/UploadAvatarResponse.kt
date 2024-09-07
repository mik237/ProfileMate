package me.ibrahim.profilemate.data.dto

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class UploadAvatarResponse(
    @SerializedName("avatar_url")
    val avatarUrl: String
) : Parcelable