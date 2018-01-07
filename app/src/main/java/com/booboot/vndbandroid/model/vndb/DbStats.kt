package com.booboot.vndbandroid.model.vndb

data class DbStats(
        var users: Int = 0,
        var threads: Int = 0,
        var tags: Int = 0,
        var releases: Int = 0,
        var producers: Int = 0,
        var chars: Int = 0,
        var posts: Int = 0,
        var vn: Int = 0,
        var traits: Int = 0
)