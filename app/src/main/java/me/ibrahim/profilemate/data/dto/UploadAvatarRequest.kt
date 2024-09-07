package me.ibrahim.profilemate.data.dto

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class UploadAvatarRequest(
    @SerializedName("avatar")
    val avatar: String,

    //this url will be returned in the response
    @SerializedName("avatar_url")
    val avatarUrl: String
) : Parcelable