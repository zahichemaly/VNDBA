package com.booboot.vndbandroid.util

import android.content.Context
import android.text.format.DateFormat
import com.booboot.vndbandroid.extensions.upperCase
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

const val TBA = "tba"

object Utils {
    @Throws
    private fun toDate(date: String?): Date? = date?.let {
        try {
            SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date)
        } catch (e: ParseException) {
            SimpleDateFormat("yyyy-MM", Locale.US).parse(date)
        }
    }

    fun getDate(date: String?, showFullDate: Boolean) = processDate(date) {
        SimpleDateFormat(if (showFullDate) "d MMMM yyyy" else "yyyy", Locale.US).format(it)
    }

    fun formatDateMedium(context: Context, date: String?) = processDate(date) {
        DateFormat.getMediumDateFormat(context).format(it)
    }

    private fun processDate(date: String?, convert: (Date) -> String?) = try {
        toDate(date)?.let { convert(it) } ?: "Unknown"
    } catch (e: ParseException) {
        if (date == TBA) date.upperCase() else date
    }
}