package com.booboot.vndbandroid.extensions

import com.booboot.vndbandroid.model.vndbandroid.AccountItems
import com.crashlytics.android.Crashlytics

// TODO GDPR
object Track {
    fun tag(accountItems: AccountItems) {
        Crashlytics.setInt("VNS SIZE", accountItems.vns.size)
        Crashlytics.setInt("VNLIST SIZE", accountItems.userList.size)
    }
}