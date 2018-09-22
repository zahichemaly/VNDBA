package com.booboot.vndbandroid

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkRequest
import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import androidx.preference.PreferenceManager
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.booboot.vndbandroid.di.AppComponent
import com.booboot.vndbandroid.di.AppModule
import com.booboot.vndbandroid.di.DaggerAppComponent
import com.booboot.vndbandroid.extensions.log
import com.booboot.vndbandroid.model.vndbandroid.Preferences
import com.booboot.vndbandroid.service.ConnectionReceiver
import com.booboot.vndbandroid.util.Notifications
import com.chibatching.kotpref.Kotpref
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import io.fabric.sdk.android.Fabric
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.android.schedulers.AndroidSchedulers
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

        PreferenceManager.setDefaultValues(this, Preferences.kotprefName, Context.MODE_PRIVATE, R.xml.app_preferences, false)
        AppCompatDelegate.setDefaultNightMode(Preferences.nightMode)
        Notifications.createNotificationChannels(this)

        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        connectivityManager?.registerNetworkCallback(NetworkRequest.Builder().build(), ConnectionReceiver())

        /* Prevents RxJava from crashing the app when there is an exception and let all onError() handle them */
        RxJavaPlugins.setErrorHandler { it.log() }

        /* Speeding up Rx's main thread scheduling (RxAndroid 2.1.0's optimization) */
        val asyncMainThreadScheduler = AndroidSchedulers.from(Looper.getMainLooper(), true)
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { asyncMainThreadScheduler }
        RxAndroidPlugins.setMainThreadSchedulerHandler { asyncMainThreadScheduler }
    }

    companion object {
        lateinit var context: App
    }
}