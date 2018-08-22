package com.booboot.vndbandroid.extensions

import android.text.Spanned
import androidx.core.text.HtmlCompat
import com.booboot.vndbandroid.model.vndb.Tag

fun String.format(urlScheme: String): Spanned {
    var formatted = replace("\\[url=(.*)](.*)\\[/url]".toRegex(), "<a href=\"$urlScheme://$1\">$2</a>")
        .replace("\n", "<br/>")

    if (!Tag.checkSpoilerLevel(0, 2)) {
        formatted = formatted.replace("\\[spoiler][\\s\\S]*\\[/spoiler]".toRegex(), "")
    }

    return HtmlCompat.fromHtml(formatted, HtmlCompat.FROM_HTML_MODE_COMPACT)
}