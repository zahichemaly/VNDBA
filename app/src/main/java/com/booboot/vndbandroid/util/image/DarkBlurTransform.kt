package com.booboot.vndbandroid.util.image

import android.content.Context
import android.graphics.Bitmap

import com.squareup.picasso.Transformation

class DarkBlurTransform(private val context: Context, private val blur: Boolean) : Transformation {
    override fun transform(bitmap: Bitmap): Bitmap = if (blur || DEMO) {
        val res = BitmapTransformation.darkBlur(context, bitmap)
        bitmap.recycle()
        res
    } else bitmap

    override fun key(): String {
        return "darkblur"
    }

    companion object {
        private const val DEMO = false
    }
}