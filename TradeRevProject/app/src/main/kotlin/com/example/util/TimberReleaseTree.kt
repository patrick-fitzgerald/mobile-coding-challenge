package com.example.util

import android.util.Log
import timber.log.Timber

class TimberReleaseTree : Timber.Tree() {

    override fun isLoggable(tag: String?, priority: Int): Boolean {
        return priority >= Log.INFO
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        Log.println(priority, tag, message)
    }
}
