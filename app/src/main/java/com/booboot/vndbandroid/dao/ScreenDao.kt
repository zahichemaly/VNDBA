package com.booboot.vndbandroid.dao

import com.booboot.vndbandroid.model.vndb.Screen
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
class ScreenDao() {
    @Id var id: Long = 0
    var image: String = ""
    var rid: Int = 0
    var nsfw: Boolean = false
    var height: Int = 0
    var width: Int = 0

    constructor(screen: Screen) : this() {
        image = screen.image
        rid = screen.rid
        nsfw = screen.nsfw
        height = screen.height
        width = screen.width
    }

    fun toBo() = Screen(image, rid, nsfw, height, width)
}