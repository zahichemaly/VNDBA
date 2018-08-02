package com.booboot.vndbandroid.extensions

import com.booboot.vndbandroid.BuildConfig
import com.crashlytics.android.Crashlytics

fun Throwable.errorMessage(): String = cause?.errorMessage() ?: localizedMessage ?: toString()

fun Throwable.log() =
    if (BuildConfig.DEBUG) printStackTrace()
    else Crashlytics.logException(this)