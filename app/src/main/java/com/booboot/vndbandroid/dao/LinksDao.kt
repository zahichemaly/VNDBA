package com.booboot.vndbandroid.dao

import com.booboot.vndbandroid.model.vndb.Links
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
class LinksDao() {
    @Id var id: Long = 0
    var wikipedia: String? = null
    var encubed: String? = null
    var renai: String? = null
    var homepage: String? = null
    var twitter: String? = null
    var anidb: String? = null

    constructor(links: Links) : this() {
        wikipedia = links.wikipedia
        encubed = links.encubed
        renai = links.renai
        homepage = links.homepage
        twitter = links.twitter
        anidb = links.anidb
    }

    fun toBo() = Links(wikipedia, encubed, renai, homepage, twitter, anidb)
}