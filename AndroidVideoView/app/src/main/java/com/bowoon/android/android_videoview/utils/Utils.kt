package com.bowoon.android.android_videoview.utils

import android.content.Context
import android.content.res.Resources
import android.net.Uri
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager
import java.text.SimpleDateFormat
import java.util.*


object Utils {
    fun getDisplayMetrics(context: Context): DisplayMetrics {
        return context.resources.displayMetrics
    }

    fun getWindowManager(context: Context): WindowManager {
        return context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    fun getTimeString(millis: Int?): String {
        millis?.let {
            val buf = StringBuffer()
            val hours = (millis / (1000 * 60 * 60))
            val minutes = (millis % (1000 * 60 * 60) / (1000 * 60))
            val seconds = (millis % (1000 * 60 * 60) % (1000 * 60) / 1000)
            buf
                    .append(String.format(if (hours == 0) "" else "%02d:", hours))
                    .append(String.format(if (minutes == 0) "" else "%02d:", minutes))
                    .append(String.format("%02d", seconds))
            return buf.toString()
        }
        return ""
    }

    fun getPathFromUri(context: Context, uri: Uri?): String? {
        uri?.let {
            context.contentResolver.query(it, null, null, null, null)?.use { cursor ->
                cursor.moveToNext()
                return cursor.getString(cursor.getColumnIndex("_data"))
            }
        }
        return null
    }
}

val Int.px
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics).toInt()