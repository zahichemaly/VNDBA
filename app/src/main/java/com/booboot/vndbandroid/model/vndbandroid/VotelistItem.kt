package com.booboot.vndbandroid.model.vndbandroid

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class VotelistItem : CacheItem {
    var vote: Int = 0

    constructor()

    constructor(vn: Int, added: Int, vote: Int) : super(vn, added) {
        this.vote = vote
    }
}
