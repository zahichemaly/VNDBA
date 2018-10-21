package com.booboot.vndbandroid.model.vndbandroid

import androidx.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@IntDef(SORT_ID, SORT_TITLE, SORT_RELEASE_DATE, SORT_LENGTH, SORT_POPULARITY, SORT_RATING, SORT_STATUS, SORT_VOTE, SORT_PRIORITY)
annotation class SortOptions

const val SORT_ID = 0
const val SORT_TITLE = 1
const val SORT_RELEASE_DATE = 2
const val SORT_LENGTH = 3
const val SORT_POPULARITY = 4
const val SORT_RATING = 5
const val SORT_STATUS = 6
const val SORT_VOTE = 7
const val SORT_PRIORITY = 8