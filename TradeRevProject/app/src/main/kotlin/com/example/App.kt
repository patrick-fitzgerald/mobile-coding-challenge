package com.example

import android.app.Application
import com.example.di.appModules
import com.example.util.TimberDebugTree
import com.example.util.TimberReleaseTree
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

open class App : Application() {
    override fun onCreate() {
        super.onCreate()

        initTimber()
        initKoin()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(TimberDebugTree())
        } else {
            Timber.plant(TimberReleaseTree())
        }
    }

    open fun initKoin() {
        startKoin {
            // Android context
            androidContext(this@App)
            // logger
            androidLogger()
            // modules
            modules(appModules)
        }
    }
}
