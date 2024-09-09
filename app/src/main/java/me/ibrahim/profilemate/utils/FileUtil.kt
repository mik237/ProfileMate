package me.ibrahim.profilemate.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.core.content.FileProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FileUtil @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val MAX_SIZE_BYTES = 1 * 1024 * 1024 // 1 MB in bytes


    fun createImageFile(): Uri {
        val fileName = "profile_pic.jpg"
        val imageFile = File(context.cacheDir, fileName)
        if (imageFile.exists().not()) {
            imageFile.createNewFile()
        }
        return FileProvider.getUriForFile(context, "${context.packageName}.provider", imageFile)
    }

    fun getBitmapFromUri(uri: Uri): Bitmap? {
        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun bitmapToFile(bitmap: Bitmap): File? {
        return try {
            val file = File(context.cacheDir, "compressed_pic.jpg")
            if (file.exists().not()) {
                file.createNewFile()
            }
            FileOutputStream(file).use { outputStream ->
                // Compress the bitmap and save to the output stream
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
            }
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getFileUri(file: File): Uri? {
        return try {
            val fileUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )
            fileUri.buildUpon()
                .appendQueryParameter("timeStamp", System.currentTimeMillis().toString())
                .build()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun convertBitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }


    fun getBase64EncodedAvatarFromUri(uri: Uri): Pair<String?, Uri?>? {

        val bitmap = try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: Exception) {
            null
        }

        bitmap?.let {
            var resizedBitmap = it
            val quality = 100
            var base64String: String?

            do {
                // Compress the bitmap into a ByteArrayOutputStream
                val outputStream = ByteArrayOutputStream()
                resizedBitmap.compress(Bitmap.CompressFormat.PNG, quality, outputStream) // Use JPEG for lossy compression
                val byteArray = outputStream.toByteArray()

                base64String = Base64.encodeToString(byteArray, Base64.DEFAULT)
                if (base64String.length <= MAX_SIZE_BYTES) {
                    break
                }

                // Reduce the size of the bitmap if the Base64 string is too large
                // Reduce the bitmap dimensions by 80% and decrease quality
                val width = (resizedBitmap.width * 0.8).toInt()
                val height = (resizedBitmap.height * 0.8).toInt()
                resizedBitmap = Bitmap.createScaledBitmap(resizedBitmap, width, height, true)

            } while (byteArray.size > MAX_SIZE_BYTES)

            val file = bitmapToFile(resizedBitmap)
            val fileUri = file?.let { f -> getFileUri(f) }
            return Pair(base64String, fileUri)
        }

        return null
    }
    /*
        suspend fun compressImage(file: File): File {
            return Compressor.compress(context, file) {
                quality(90)
                size(1_048_576)
            }
        }*/
}