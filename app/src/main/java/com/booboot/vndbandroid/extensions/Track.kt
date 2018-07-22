package com.booboot.vndbandroid.extensions

import com.booboot.vndbandroid.model.vndb.AccountItems
import com.crashlytics.android.Crashlytics

// TODO GDPR
object Track {
    fun tag(accountItems: AccountItems) {
        Crashlytics.setInt("VNS SIZE", accountItems.vns.size)
        Crashlytics.setInt("VNLIST SIZE", accountItems.vnlist.size)
        Crashlytics.setInt("VOTELIST SIZE", accountItems.votelist.size)
        Crashlytics.setInt("WISHLIST SIZE", accountItems.wishlist.size)
    }
}