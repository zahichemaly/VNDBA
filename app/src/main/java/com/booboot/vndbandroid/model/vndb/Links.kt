package com.booboot.vndbandroid.model.vndb

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Links(
    var wikipedia: String? = null,
    var encubed: String? = null,
    var renai: String? = null,
    var homepage: String? = null,
    var twitter: String? = null,
    var anidb: String? = null
) {
    companion object {
        const val WIKIPEDIA = "https://en.wikipedia.org/wiki/"
        const val ENCUBED = "http://novelnews.net/tag/"
        const val RENAI = "http://renai.us/game/"
        const val ANIDB = "https://anidb.net/perl-bin/animedb.pl?show=anime&aid="
        const val VNDB = "https://vndb.org"
        const val VNDB_REGISTER = "$VNDB/u/register"
        const val VNDB_PAGE = "$VNDB/v"
        const val VNDB_API = "$VNDB/api/"
        const val GITHUB = "https://github.com/herbeth1u/VNDB-Android"
        const val PLAY_STORE = "https://play.google.com/store/apps/details?id=com.booboot.vndbandroid"
        const val VNSTAT = "https://vnstat.net/"
        const val EMAIL = "vndba.app@gmail.com"
        const val PRIVACY_POLICY = "https://thomas_herbeth.gitlab.io/VNDBA/privacy"
    }
}