package com.example.util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class PreferenceHelper(context: Context) {

    companion object {
        private const val prefsFileName = "com.example.prefs"
        private const val usernameConst = "username"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(prefsFileName, MODE_PRIVATE)

    var username: String?
        get() = prefs.getString(usernameConst, "")
        set(value) = prefs.edit().putString(usernameConst, value).apply()
}
