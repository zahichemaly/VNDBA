package com.booboot.vndbandroid.util

import android.content.Context
import android.text.format.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    private fun toDate(date: String?): Date? = if (date == null) {
        null
    } else try {
        SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date)
    } catch (e: ParseException) {
        null
    }

    fun getDate(date: String?, showFullDate: Boolean): String? = toDate(date)?.let {
        SimpleDateFormat(if (showFullDate) "d MMMM yyyy" else "yyyy", Locale.US).format(it)
    } ?: "Unknown"

    fun formatDateMedium(context: Context, date: String?): String = toDate(date)?.let {
        DateFormat.getMediumDateFormat(context).format(it)
    } ?: "Unknown"
}