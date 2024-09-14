package me.ibrahim.profilemate.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Base64
import androidx.core.content.FileProvider
import coil.imageLoader
import coil.request.ImageRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private const val MAX_SIZE_BYTES = 1 * 1024 * 1024 // 1 MB in bytes

@Singleton
class FileUtil @Inject constructor(@ApplicationContext private val context: Context) {

    fun createImageFile(): Uri {
        val fileName = "profile_pic.jpg"
        val imageFile = File(context.cacheDir, fileName)
        if (imageFile.exists().not()) {
            imageFile.createNewFile()
        }
        return FileProvider.getUriForFile(context, "${context.packageName}.provider", imageFile)
    }


    // Load the image, resize it, convert to Bitmap, and return Base64 string & local Uri
    suspend fun loadImageAndConvertToBase64(uri: Uri): Pair<String?, String?> = suspendCoroutine {
        val imageLoader = context.imageLoader
        val request = ImageRequest.Builder(context)
            .data(uri)
            .size(600)
            .target { drawable ->
                val bitmap = (drawable as BitmapDrawable).bitmap
                val base64 = bitmapToBase64(bitmap)
                val fileUri = saveImageToInternalStorage(bitmap)
                it.resume(Pair(base64, fileUri.toString()))
            }.build()

        imageLoader.enqueue(request)
    }

    private fun bitmapToBase64(bitmap: Bitmap?): String? {
        bitmap?.let {
            var resizedBitmap = it
            val quality = 90
            var base64String: String?

            do {
                // Compress the bitmap into a ByteArrayOutputStream
                val outputStream = ByteArrayOutputStream()
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream) // Use JPEG for lossy compression
                val byteArray = outputStream.toByteArray()

                base64String = Base64.encodeToString(byteArray, Base64.DEFAULT)
                if (base64String.length <= MAX_SIZE_BYTES) {
                    break
                }

                // Reduce the size of the bitmap if the Base64 string is too large
                // Reduce the bitmap dimensions by 90% and decrease quality
                val width = (resizedBitmap.width * 0.9).toInt()
                val height = (resizedBitmap.height * 0.9).toInt()
                resizedBitmap = Bitmap.createScaledBitmap(resizedBitmap, width, height, true)

            } while (byteArray.size > MAX_SIZE_BYTES)
            return base64String
        }
        return null
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): Uri? {
        return try {
            val file = File(context.cacheDir, "profile_pic.jpg")
            if (file.exists().not()) {
                file.createNewFile()
            }
            FileOutputStream(file).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            }
            Uri.fromFile(file).buildUpon()
                .appendQueryParameter("timeStamp", System.currentTimeMillis().toString())
                .build()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /*
        suspend fun compressImage(file: File): File {
            return Compressor.compress(context, file) {
                quality(90)
                size(1_048_576)
            }
        }*/
}