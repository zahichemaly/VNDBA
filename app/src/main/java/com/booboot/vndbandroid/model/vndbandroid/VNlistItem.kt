package com.booboot.vndbandroid.model.vndbandroid

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class VNlistItem : CacheItem {
    var status: Int = 0
    var notes: String? = null

    constructor()

    constructor(vn: Int, added: Int, status: Int, notes: String?) : super(vn, added) {
        this.status = status
        this.notes = notes
    }

    override fun toString(): String {
        return "VNlistItem{" +
                "status=" + status +
                ", notes='" + notes + '\'' +
                '}'
    }
}