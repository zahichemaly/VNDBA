package com.booboot.vndbandroid.model.vndbandroid

import androidx.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@IntDef(FLAGS_NOT_EXISTS, FLAGS_BASIC, FLAGS_DETAILS, FLAGS_FULL)
annotation class ApiFlags

const val FLAGS_NOT_EXISTS = -1
const val FLAGS_BASIC = 0
const val FLAGS_DETAILS = 1
const val FLAGS_FULL = 10