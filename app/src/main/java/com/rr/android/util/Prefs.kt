package com.rr.android.util

import android.content.SharedPreferences
import javax.inject.Inject

class Prefs @Inject constructor(private val prefs: SharedPreferences) {

    val ACCESS_PIN = "access_pin"
    val FINGER_PRINT = "use_finger_print"

    var accessPin: String
        get() = prefs.getString(ACCESS_PIN, STRING_EMPTY) ?: STRING_EMPTY
        set(value) = prefs.edit().putString(ACCESS_PIN, value).apply()

    var useFingerPrint: Boolean
        get() = prefs.getBoolean(FINGER_PRINT, false)
        set(value) = prefs.edit().putBoolean(FINGER_PRINT, value).apply()

    fun clear() = prefs.edit().clear().apply()
}
