package com.booboot.vndbandroid.model.vndb

import android.app.Activity
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import com.booboot.vndbandroid.util.Utils

class Links {
    var wikipedia: String? = null
    var encubed: String? = null
    var renai: String? = null
    var homepage: String? = null
    var twitter: String? = null
    var anidb: String? = null

    companion object {
        val WIKIPEDIA = "https://en.wikipedia.org/wiki/"
        val ENCUBED = "http://novelnews.net/tag/"
        val RENAI = "http://renai.us/game/"
        val ANIDB = "https://anidb.net/perl-bin/animedb.pl?show=anime&aid="
        val VNDB = "https://vndb.org"
        val VNDB_REGISTER = "$VNDB/u/register"
        val VNDB_PAGE = "$VNDB/v"
        val VNDB_API = "$VNDB/api/"
        val GITHUB = "https://github.com/herbeth1u/VNDB-Android"
        val PLAY_STORE = "https://play.google.com/store/apps/details?id=com.booboot.vndbandroid"
        val VNSTAT = "https://vnstat.net/"
        val EMAIL = "vndba.app@gmail.com"

        fun setTextViewLink(context: Activity, textView: TextView, url: String, start: Int, end: Int) {
            val ss = SpannableString(textView.text)
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(textView: View) {
                    Utils.openURL(context, url)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                }
            }
            ss.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            textView.text = ss
            textView.movementMethod = LinkMovementMethod.getInstance()
            textView.highlightColor = Color.TRANSPARENT
        }
    }
}
