package com.bowoon.android.android_videoview.activites.vm

import android.media.MediaPlayer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bowoon.android.android_videoview.model.Video

class VideoPlayerActivityVM : ViewModel() {
    val isPlay = MutableLiveData<Boolean>()
    val playTime = MutableLiveData<Int>(0)
    val video = MutableLiveData<Video>()
    val player = MutableLiveData<MediaPlayer>(MediaPlayer())
    val orientation = MutableLiveData<Unit>(Unit)
}