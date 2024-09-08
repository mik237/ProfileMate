package me.ibrahim.profilemate.presentation.profile_ui

import android.net.Uri

sealed class ProfileEvents {
    data object CreateImageFile : ProfileEvents()
    data class UploadAvatar(val uri: Uri) : ProfileEvents()
    data object GetProfileFromRemote : ProfileEvents()
    data object ReadProfileFromLocal : ProfileEvents()
}