package me.ibrahim.profilemate.utils

import android.graphics.Bitmap
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import androidx.core.graphics.applyCanvas

import androidx.core.graphics.createBitmap
import coil.size.Size
import coil.transform.Transformation

class GrayScaleTransformation : Transformation {

    private companion object {
        val COLOR_FILTER = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0.3f) })
    }

    override val cacheKey: String
        get() = "profileMatePic"

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val paint = Paint()
        paint.colorFilter = COLOR_FILTER
        val output = createBitmap(input.width, input.height, input.config)

        output.applyCanvas {
            drawBitmap(input, 0f, 0f, paint)
        }
        input.recycle()
        return output
    }
}