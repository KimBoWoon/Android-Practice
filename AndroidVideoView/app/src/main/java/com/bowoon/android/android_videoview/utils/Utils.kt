package com.bowoon.android.android_videoview.utils

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager


object Utils {
    fun getDisplayMetrics(context: Context): DisplayMetrics {
        return context.resources.displayMetrics
    }

    fun getWindowManager(context: Context): WindowManager {
        return context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }
}

fun Int.px(): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics).toInt()