package com.booboot.vndbandroid.model.vndbandroid

import android.graphics.Bitmap
import android.net.Uri

data class FileAction(
    val uri: Uri,
    val bitmap: Bitmap,
    val action: Int
)