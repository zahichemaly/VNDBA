package com.booboot.vndbandroid

import android.app.Application
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.booboot.vndbandroid.di.AppComponent
import com.booboot.vndbandroid.di.DaggerAppComponent
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import io.fabric.sdk.android.Fabric
import io.realm.Realm

class App : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this

        appComponent = DaggerAppComponent.create()
        Realm.init(this)

        CaocConfig.Builder.create()
                .errorDrawable(R.mipmap.ic_launcher)
                .apply()

        val core = CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()
        Fabric.with(this, Crashlytics.Builder().core(core).build())
    }

    companion object {
        lateinit var instance: App
    }
}