package com.booboot.vndbandroid

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.multidex.MultiDexApplication
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.booboot.vndbandroid.api.ConnectionReceiver
import com.booboot.vndbandroid.di.AppComponent
import com.booboot.vndbandroid.di.AppModule
import com.booboot.vndbandroid.di.DaggerAppComponent
import com.chibatching.kotpref.Kotpref
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import io.fabric.sdk.android.Fabric
import io.reactivex.plugins.RxJavaPlugins

class App : MultiDexApplication() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        context = this

        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
        Kotpref.init(this)

        CaocConfig.Builder.create()
                .errorDrawable(R.mipmap.ic_launcher)
                .apply()

        val core = CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()
        Fabric.with(this, Crashlytics.Builder().core(core).build())

        val connectionReceiver = ConnectionReceiver()
        registerReceiver(connectionReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        /* Prevents RxJava from crashing the app when there is an exception and let all onError() handle them */
        RxJavaPlugins.setErrorHandler { it.printStackTrace() }
    }

    companion object {
        lateinit var context: App
    }
}