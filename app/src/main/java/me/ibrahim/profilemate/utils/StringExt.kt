package me.ibrahim.profilemate.utils


import java.security.MessageDigest


fun String.sha256(): String {
    val digest = MessageDigest.getInstance(AppConstants.SHA_256)
    val bytes = digest.digest(this.trim().lowercase().toByteArray(Charsets.UTF_8))
    return bytes.fold("") { str, it -> str + "%02x".format(it) }
}

fun String.getGravatarUrl(): String {
    return "${AppConstants.GRAVATAR_BASEURL}${this.sha256()}?d=404&s=400"
}