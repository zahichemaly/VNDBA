package com.booboot.vndbandroid.extensions

import android.text.Spanned
import android.util.Log
import androidx.core.text.HtmlCompat
import com.booboot.vndbandroid.BuildConfig
import com.booboot.vndbandroid.api.VNDBServer
import com.booboot.vndbandroid.model.vndb.Tag

fun String.format(urlScheme: String): Spanned {
    var formatted = replace("\\[url=(.*)](.*)\\[/url]".toRegex(), "<a href=\"$urlScheme://$1\">$2</a>")
        .replace("\n", "<br/>")

    if (!Tag.checkSpoilerLevel(0, 2)) {
        formatted = formatted.replace("\\[spoiler][\\s\\S]*\\[/spoiler]".toRegex(), "")
    }

    return HtmlCompat.fromHtml(formatted, HtmlCompat.FROM_HTML_MODE_COMPACT)
}

fun String.toBooleanOrFalse(): Boolean = this == "1" || equals("true", true)

fun String.log() {
    if (!BuildConfig.DEBUG) return
    if (length > 4000) {
        val chunkCount = length / 4000     // integer division
        for (i in 0..chunkCount) {
            val max = 4000 * (i + 1)
            if (max >= length) {
                Log.e(VNDBServer.CLIENT, substring(4000 * i))
            } else {
                Log.e(VNDBServer.CLIENT, substring(4000 * i, max))
            }
        }
    } else {
        Log.e(VNDBServer.CLIENT, this)
    }
}