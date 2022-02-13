package com.rr.android.util.extensions

import android.view.View

fun View.runAfterInactivityPeriod(runnable: Runnable, period: Long) {
    removeCallbacks(runnable)
    postDelayed(runnable, period)
}
