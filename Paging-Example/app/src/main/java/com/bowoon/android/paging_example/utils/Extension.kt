package com.bowoon.android.paging_example.utils

import android.content.res.Resources
import kotlin.math.roundToInt

val Int.dp: Int
    get() = (this.toFloat() / Resources.getSystem().displayMetrics.density).roundToInt()