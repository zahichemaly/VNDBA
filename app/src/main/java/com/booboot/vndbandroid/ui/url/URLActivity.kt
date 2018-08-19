package com.booboot.vndbandroid.ui.url

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.booboot.vndbandroid.extensions.openURL

class URLActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.data?.toString()?.substring(packageName.length + 3)?.let {
            openURL(it)
        }
        finish()
    }
}