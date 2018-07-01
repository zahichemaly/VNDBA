package com.booboot.vndbandroid.util.image

import android.content.Context
import android.graphics.*
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur

object BitmapTransformation {
    fun darkBlur(context: Context, bitmap: Bitmap): Bitmap {
        return darken(blur(context, bitmap))
    }

    fun blur(context: Context, bitmap: Bitmap): Bitmap {
        val rs = RenderScript.create(context)

        // Create another bitmap that will hold the results of the filter.
        val blurredBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)

        // Allocate memory for Renderscript to work with
        val input = Allocation.createFromBitmap(rs, blurredBitmap, Allocation.MipmapControl.MIPMAP_FULL, Allocation.USAGE_SHARED)
        val output = Allocation.createTyped(rs, input.type)

        // Load up an instance of the specific script that we want to use.
        val script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        script.setInput(input)

        // Set the blur radius
        script.setRadius(25f)

        // Start the ScriptIntrinisicBlur
        script.forEach(output)

        // Copy the output to the blurred bitmap
        output.copyTo(blurredBitmap)

        return blurredBitmap
    }

    fun darken(bitmap: Bitmap): Bitmap {
        val canvas = Canvas(bitmap)
        val p = Paint(Color.RED)
        //ColorFilter filter = new LightingColorFilter(0xFFFFFFFF , 0x00222222); // lighten
        val filter = LightingColorFilter(-0x808081, 0x00000000)    // darken
        p.colorFilter = filter
        canvas.drawBitmap(bitmap, Matrix(), p)

        return bitmap
    }
}