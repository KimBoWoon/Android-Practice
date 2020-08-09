package com.bowoon.android.android_videoview.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

object Utils {
    private var displayMetrics: DisplayMetrics? = null

    fun getDisplayMetrics(context: Context): DisplayMetrics? {
        if (displayMetrics == null) {
            displayMetrics = context.resources.displayMetrics
        }
        return displayMetrics
    }

    fun getWindowManager(context: Context): WindowManager? {
        return context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }
}