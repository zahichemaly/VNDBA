package com.booboot.vndbandroid

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkRequest
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import androidx.preference.PreferenceManager
import androidx.transition.Transition
import androidx.transition.TransitionInflater
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.booboot.vndbandroid.di.AppComponent
import com.booboot.vndbandroid.di.AppModule
import com.booboot.vndbandroid.di.DaggerAppComponent
import com.booboot.vndbandroid.model.vndbandroid.Preferences
import com.booboot.vndbandroid.model.vndbandroid.YES
import com.booboot.vndbandroid.service.ConnectionReceiver
import com.booboot.vndbandroid.util.Notifications
import com.chibatching.kotpref.Kotpref
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import io.fabric.sdk.android.Fabric

class App : MultiDexApplication() {
    lateinit var appComponent: AppComponent
    lateinit var enterTransition: Transition

    override fun onCreate() {
        super.onCreate()
        context = this

        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
        Kotpref.init(this)

        CaocConfig.Builder.create()
            .errorDrawable(R.mipmap.ic_launcher)
            .apply()

        PreferenceManager.setDefaultValues(this, Preferences.kotprefName, Context.MODE_PRIVATE, R.xml.app_preferences, Preferences.shouldResetPreferences)
        Preferences.shouldResetPreferences = false
        AppCompatDelegate.setDefaultNightMode(Preferences.nightMode)
        Notifications.createNotificationChannels(this)

        val core = CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG || Preferences.gdprCrashlytics != YES).build()
        Fabric.with(this, Crashlytics.Builder().core(core).build())

        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        connectivityManager?.registerNetworkCallback(NetworkRequest.Builder().build(), ConnectionReceiver())

        enterTransition = TransitionInflater.from(this).inflateTransition(android.R.transition.move)
    }

    companion object {
        lateinit var context: App
    }
}