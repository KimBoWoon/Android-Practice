package com.bowoon.android.android_videoview.activites.vm

import android.content.Context
import android.media.MediaPlayer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bowoon.android.android_videoview.model.Video
import com.bowoon.android.android_videoview.utils.Utils

class VideoPlayerActivityVM : ViewModel() {
    val isPlay = MutableLiveData<Boolean>()
    val video = MutableLiveData<Video>()
    val resize = MutableLiveData<Pair<Int, Int>>(0 to 0)

    fun resizeSurfaceView(context: Context, player: MediaPlayer) {
        var newWidth = player.videoWidth
        var newHeight = player.videoHeight
        val rate: Float
        val max = if (newWidth > newHeight) {
            Utils.getDisplayMetrics(context).widthPixels
        } else {
            Utils.getDisplayMetrics(context).heightPixels
        }.toFloat()

        if (newWidth == 0 || newHeight == 0) {
            return
        }

        if (newWidth > newHeight) {
            rate = max / newWidth
            newHeight = (newHeight * rate).toInt()
            newWidth = max.toInt()
        } else {
            rate = max / newHeight
            newWidth = (newWidth * rate).toInt()
            newHeight = max.toInt()
        }

        if (newHeight >= Utils.getDisplayMetrics(context).heightPixels) {
            newHeight = Utils.getDisplayMetrics(context).heightPixels
        }

        if (newWidth >= Utils.getDisplayMetrics(context).widthPixels) {
            newWidth = Utils.getDisplayMetrics(context).widthPixels
        }

        resize.value = newWidth to newHeight
    }
}