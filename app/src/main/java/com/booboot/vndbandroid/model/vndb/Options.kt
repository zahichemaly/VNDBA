package com.booboot.vndbandroid.model.vndb

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Options(var page: Int = 1,
                   var results: Int = 25,
                   var sort: String? = null,
                   var reverse: Boolean = false,
                   var fetchAllPages: Boolean = false,
                   var numberOfPages: Int = 0) : VNDBCommand() {

    companion object {
        fun create(fetchAllPages: Boolean, numberOfPages: Int): Options {
            val options = Options()
            options.fetchAllPages = fetchAllPages
            options.numberOfPages = numberOfPages
            return options
        }

        fun create(page: Int, results: Int, sort: String?, reverse: Boolean, fetchAllPages: Boolean, numberOfPages: Int): Options {
            val options = Options()
            options.page = page
            options.results = results
            options.sort = sort
            options.reverse = reverse
            options.fetchAllPages = fetchAllPages
            options.numberOfPages = numberOfPages
            return options
        }

        fun create(other: Options): Options {
            val options = Options()
            options.page = other.page
            options.results = other.results
            options.sort = other.sort
            options.reverse = other.reverse
            options.fetchAllPages = other.fetchAllPages
            options.numberOfPages = other.numberOfPages
            return options
        }
    }
}