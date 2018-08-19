package com.booboot.vndbandroid.extensions

import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.core.text.HtmlCompat
import com.booboot.vndbandroid.model.vndb.Tag

fun TextView.formatText(raw: String?) {
    var formatted = raw?.replace("\\[url=(.*)](.*)\\[/url]".toRegex(), "<a href=\"" + context.packageName + "://$1\">$2</a>")
        ?.replace("\n", "<br/>")
        ?: ""

    if (!Tag.checkSpoilerLevel(0, 2)) {
        formatted = formatted.replace("\\[spoiler][\\s\\S]*\\[/spoiler]".toRegex(), "")
    }

    if (formatted.contains("</a>"))
        movementMethod = LinkMovementMethod.getInstance()
    text = HtmlCompat.fromHtml(formatted, HtmlCompat.FROM_HTML_MODE_COMPACT)
}