package com.bowoon.android.android_videoview.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.Toast

object Utils {
    fun getDisplayMetrics(context: Context): DisplayMetrics {
        return context.resources.displayMetrics
    }

    fun getWindowManager(context: Context): WindowManager {
        return context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }
}