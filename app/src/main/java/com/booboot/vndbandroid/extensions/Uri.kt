package com.booboot.vndbandroid.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri

fun Uri.grantPermission(context: Context) {
    context.grantUriPermission(context.packageName, this, Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
}