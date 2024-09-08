package me.ibrahim.profilemate.utils

enum class ImagePickerOption {
    CAMERA,
    GALLERY
}


enum class HttpCodes(val code: Int) {
    SESSION_EXPIRED(419),
    NO_INTERNET_CONNECTION(500)
}