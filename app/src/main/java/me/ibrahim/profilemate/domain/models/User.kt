package me.ibrahim.profilemate.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class User(
    val userId: String,
    val email: String,
    val password: String,
    val avatarUrl: String
) : Parcelable
