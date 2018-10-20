package com.booboot.vndbandroid.model.vndbandroid

import androidx.annotation.StringDef

@Retention(AnnotationRetention.SOURCE)
@StringDef(EVENT_VNLIST_CHANGED)
annotation class AppEvents

const val EVENT_VNLIST_CHANGED = "EVENT_VNLIST_CHANGED"
