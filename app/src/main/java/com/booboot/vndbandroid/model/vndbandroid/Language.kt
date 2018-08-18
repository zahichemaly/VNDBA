package com.booboot.vndbandroid.model.vndbandroid

import androidx.annotation.DrawableRes
import com.booboot.vndbandroid.R

data class Language(
    val text: String = "",
    @DrawableRes val flag: Int = 0
)

val LANGUAGES = mapOf(
    "ar" to Language("Arabic", R.drawable.ic_saudi_arabia),
    "ca" to Language("Catalan", R.drawable.ic_balearic_islands),
    "cs" to Language("Czech", R.drawable.ic_czech_republic),
    "da" to Language("Danish", R.drawable.ic_denmark),
    "de" to Language("German", R.drawable.ic_germany),
    "en" to Language("English", R.drawable.ic_united_kingdom),
    "es" to Language("Spanish", R.drawable.ic_spain),
    "fi" to Language("Finnish", R.drawable.ic_finland),
    "fr" to Language("French", R.drawable.ic_france),
    "he" to Language("Hebrew", R.drawable.ic_israel),
    "hu" to Language("Hungarian", R.drawable.ic_hungary),
    "id" to Language("Indonesian", R.drawable.ic_indonesia),
    "it" to Language("Italian", R.drawable.ic_italy),
    "ja" to Language("Japanese", R.drawable.ic_japan),
    "ko" to Language("Korean", R.drawable.ic_south_korea),
    "nl" to Language("Dutch", R.drawable.ic_netherlands),
    "no" to Language("Norwegian", R.drawable.ic_norway),
    "pl" to Language("Polish", R.drawable.ic_republic_of_poland),
    "pt-br" to Language("Portuguese (Brazil)", R.drawable.ic_brazil),
    "pt-pt" to Language("Portuguese (Portugal)", R.drawable.ic_portugal),
    "ro" to Language("Romanian", R.drawable.ic_romania),
    "ru" to Language("Russian", R.drawable.ic_russia),
    "sk" to Language("Slovak", R.drawable.ic_slovakia),
    "sv" to Language("Swedish", R.drawable.ic_sweden),
    "ta" to Language("Tagalog", R.drawable.ic_philippines),
    "tr" to Language("Turkish", R.drawable.ic_turkey),
    "uk" to Language("Ukrainian", R.drawable.ic_ukraine),
    "vi" to Language("Vietnamese", R.drawable.ic_vietnam),
    "zh" to Language("Chinese", R.drawable.ic_china)
)