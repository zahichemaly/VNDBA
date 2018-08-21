package com.booboot.vndbandroid.extensions

import com.booboot.vndbandroid.model.vndb.Tag

fun String.format(urlScheme: String): String {
    var formatted = replace("\\[url=(.*)](.*)\\[/url]".toRegex(), "<a href=\"$urlScheme://$1\">$2</a>")
        .replace("\n", "<br/>")

    if (!Tag.checkSpoilerLevel(0, 2)) {
        formatted = formatted.replace("\\[spoiler][\\s\\S]*\\[/spoiler]".toRegex(), "")
    }

    return formatted
}