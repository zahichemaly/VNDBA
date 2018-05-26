package com.booboot.vndbandroid.util.image

import android.content.Context
import android.graphics.Bitmap

import com.squareup.picasso.Transformation

class BlurIfDemoTransform(private val context: Context) : Transformation {
    override fun transform(bitmap: Bitmap): Bitmap {
        if (!DEMO) return bitmap
        val res = BitmapTransformation.darkBlur(context, bitmap)
        bitmap.recycle()
        return res
    }

    override fun key(): String {
        return "darkblur"
    }

    companion object {
        private val DEMO = false
    }
}