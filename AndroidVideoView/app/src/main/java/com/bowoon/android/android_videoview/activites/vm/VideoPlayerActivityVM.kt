package com.bowoon.android.android_videoview.activites.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bowoon.android.android_videoview.model.Video

class VideoPlayerActivityVM : ViewModel() {
    val isPlay = MutableLiveData<Boolean>()
    val seekTime = MutableLiveData<Int>(0)
    val video = MutableLiveData<Video>()
    val resize = MutableLiveData<Unit>(Unit)
    val playTime = MutableLiveData<Int>(0)
    val seekBarFlag = MutableLiveData<Boolean>(false)
}