package com.booboot.vndbandroid.model.vndbandroid

import androidx.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@IntDef(NOT_SET, YES, NO)
annotation class GdprConsents

const val NOT_SET = 0
const val YES = 1
const val NO = 2