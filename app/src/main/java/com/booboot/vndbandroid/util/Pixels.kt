package com.booboot.vndbandroid.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import com.booboot.vndbandroid.App

object Pixels {
    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @return A float value to represent px equivalent to dp depending on device density
     */
    fun px(dp: Int): Int = Math.round(dp * App.context.resources.displayMetrics.density)

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @return A float value to represent dp equivalent to px value
     */
    fun dp(px: Int): Int = Math.round(px / App.context.resources.displayMetrics.density)

    fun spToPx(sp: Int): Int = Math.round(sp * App.context.resources.displayMetrics.scaledDensity)

    fun screenWidth(): Int = App.context.resources.displayMetrics.widthPixels

    fun textAsBitmap(text: String, textSize: Int, textColor: Int): Bitmap {
        val paint = Paint(ANTI_ALIAS_FLAG)
        paint.textSize = spToPx(textSize).toFloat()
        paint.color = textColor
        paint.textAlign = Paint.Align.LEFT
        val baseline = -paint.ascent() // ascent() is negative
        val width = (paint.measureText(text) + 0.0f).toInt() // round
        val height = (baseline + paint.descent() + 0.0f).toInt()
        val image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(image)
        canvas.drawText(text, 0f, baseline, paint)
        return image
    }
}
