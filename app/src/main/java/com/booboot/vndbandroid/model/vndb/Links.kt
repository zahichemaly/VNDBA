package com.booboot.vndbandroid.model.vndb

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class Links(var wikipedia: String? = null,
            var encubed: String? = null,
            var renai: String? = null) : VNDBCommand() {

    companion object {
        const val WIKIPEDIA = "https://en.wikipedia.org/wiki/"
        const val ENCUBED = "http://novelnews.net/tag/"
        const val RENAI = "http://renai.us/game/"
        const val ANIDB = "https://anidb.net/perl-bin/animedb.pl?show=anime&aid="
        const val VNDB_REGISTER = "https://vndb.org/u/register"
        const val VNDB = "https://vndb.org"
        const val VNDB_PAGE = "https://vndb.org/v"
        const val GITHUB = "https://github.com/herbeth1u/VNDB-Android"
        const val PLAY_STORE = "https://play.google.com/store/apps/details?id=com.booboot.vndbandroid"
        const val VNSTAT = "https://vnstat.net/"
        const val EMAIL = "vndba.app@gmail.com"
    }
}