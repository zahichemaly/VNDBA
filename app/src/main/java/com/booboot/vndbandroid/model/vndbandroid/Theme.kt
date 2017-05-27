package com.booboot.vndbandroid.model.vndbandroid

import com.booboot.vndbandroid.R

class Theme(var style: Int, var noActionBarStyle: Int, var wallpaper: Int) {
    companion object {
        val THEMES = mapOf(
                "0" to Theme(R.style.AppTheme, R.style.AppTheme_NoActionBar, R.drawable.bg_0),
                "1" to Theme(R.style.Theme1, R.style.Theme1_NoActionBar, R.drawable.bg_1),
                "2" to Theme(R.style.Theme2, R.style.Theme2_NoActionBar, R.drawable.bg_2),
                "3" to Theme(R.style.Theme3, R.style.Theme3_NoActionBar, R.drawable.bg_3),
                "4" to Theme(R.style.Theme4, R.style.Theme4_NoActionBar, R.drawable.bg_4),
                "5" to Theme(R.style.Theme5, R.style.Theme5_NoActionBar, R.drawable.bg_5),
                "6" to Theme(R.style.Theme6, R.style.Theme6_NoActionBar, R.drawable.bg_6),
                "7" to Theme(R.style.Theme7, R.style.Theme7_NoActionBar, R.drawable.bg_7),
                "8" to Theme(R.style.Theme8, R.style.Theme8_NoActionBar, R.drawable.bg_8),
                "9" to Theme(R.style.Theme9, R.style.Theme9_NoActionBar, R.drawable.bg_9),
                "10" to Theme(R.style.Theme10, R.style.Theme10_NoActionBar, R.drawable.bg_10),
                "11" to Theme(R.style.Theme11, R.style.Theme11_NoActionBar, R.drawable.bg_11),
                "12" to Theme(R.style.Theme12, R.style.Theme12_NoActionBar, R.drawable.bg_12),
                "13" to Theme(R.style.Theme13, R.style.Theme13_NoActionBar, R.drawable.bg_neon),
                "14" to Theme(R.style.Theme14, R.style.Theme14_NoActionBar, R.drawable.bg_13),
                "15" to Theme(R.style.Theme15, R.style.Theme15_NoActionBar, R.drawable.bg_14),
                "16" to Theme(R.style.Theme16, R.style.Theme16_NoActionBar, R.drawable.bg_15),
                "17" to Theme(R.style.Theme17, R.style.Theme17_NoActionBar, R.drawable.bg_16),
                "18" to Theme(R.style.Theme18, R.style.Theme18_NoActionBar, R.drawable.bg_17),
                "19" to Theme(R.style.Theme19, R.style.Theme19_NoActionBar, R.drawable.bg_18),
                "20" to Theme(R.style.Theme20, R.style.Theme20_NoActionBar, R.drawable.bg_19)
        )
    }
}
