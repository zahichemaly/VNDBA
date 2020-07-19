package com.booboot.vndbandroid.model.vndbandroid

import androidx.annotation.LongDef

@Retention(AnnotationRetention.SOURCE)
@LongDef(SORT_ID, SORT_TITLE, SORT_RELEASE_DATE, SORT_LENGTH, SORT_POPULARITY, SORT_RATING, SORT_STATUS, SORT_VOTE, SORT_PRIORITY)
annotation class SortOptions

const val SORT_ID = -10100L
const val SORT_TITLE = -10101L
const val SORT_RELEASE_DATE = -10102L
const val SORT_LENGTH = -10103L
const val SORT_POPULARITY = -10104L
const val SORT_RATING = -10105L
const val SORT_STATUS = -10106L
const val SORT_VOTE = -10107L
const val SORT_PRIORITY = -10108L