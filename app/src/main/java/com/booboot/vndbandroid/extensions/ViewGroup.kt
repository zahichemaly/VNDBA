package com.booboot.vndbandroid.extensions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import me.zhanghai.android.fastscroll.FastScrollerBuilder

fun ViewGroup.inflate(layoutRes: Int, theme: Int = -1): View {
    val context = if (theme < 0) context else ContextThemeWrapper(context, theme)
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun ViewGroup.fastScroll(config: FastScrollerBuilder.() -> Unit = {}) = FastScrollerBuilder(this).useMd2Style().apply { config() }.build()