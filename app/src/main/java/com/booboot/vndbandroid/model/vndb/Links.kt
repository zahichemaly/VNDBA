package com.booboot.vndbandroid.model.vndb

class Links {
    var wikipedia: String? = null
    var encubed: String? = null
    var renai: String? = null
    var homepage: String? = null
    var twitter: String? = null
    var anidb: String? = null

    companion object {
        val WIKIPEDIA = "https://en.wikipedia.org/wiki/"
        val ENCUBED = "http://novelnews.net/tag/"
        val RENAI = "http://renai.us/game/"
        val ANIDB = "https://anidb.net/perl-bin/animedb.pl?show=anime&aid="
        val VNDB = "https://vndb.org"
        val VNDB_REGISTER = "$VNDB/u/register"
        val VNDB_PAGE = "$VNDB/v"
        val VNDB_API = "$VNDB/api/"
        val GITHUB = "https://github.com/herbeth1u/VNDB-Android"
        val PLAY_STORE = "https://play.google.com/store/apps/details?id=com.booboot.vndbandroid"
        val VNSTAT = "https://vnstat.net/"
        val EMAIL = "vndba.app@gmail.com"
    }
}