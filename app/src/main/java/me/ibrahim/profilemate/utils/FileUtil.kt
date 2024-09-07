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

    fun compressBitmap(bitmap: Bitmap): File? {
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

    /*fun getFileFromUri(uri: Uri): File? {
            val fileName = "profile_pic.jpg"
            val tempFile = File(context.cacheDir, fileName)
            try {
                val inputStream = context.contentResolver.openInputStream(uri) ?: return null
                val outputStream = FileOutputStream(tempFile)
                copyStream(inputStream, outputStream)
                inputStream.close()
                outputStream.close()
                return tempFile
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        suspend fun compressImage(file: File): File {
            return Compressor.compress(context, file) {
                quality(90)
                size(1_048_576)
            }
        }

        private fun copyStream(inputStream: InputStream, outputStream: OutputStream) {
            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream.read(buffer).also { length = it } > 0) {
                outputStream.write(buffer, 0, length)
            }
        }*/
}