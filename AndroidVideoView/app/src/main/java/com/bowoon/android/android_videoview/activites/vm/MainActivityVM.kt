package com.bowoon.android.android_videoview.activites.vm

import android.content.Context
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bowoon.android.android_videoview.model.Video

class MainActivityVM : ViewModel() {
    val videoList = MutableLiveData<MutableList<Video>>(mutableListOf())

    fun fetchAllVideos(context: Context) {
        arrayOf(
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DATE_ADDED
        ).let {
            context.contentResolver.query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    it,
                    null,
                    null,
                    "date_added DESC"
            )?.let { cursor ->
                val dataColumnIndex = cursor.getColumnIndex(it[0])
                val nameColumnIndex = cursor.getColumnIndex(it[1])

                if (cursor.moveToFirst()) {
                    do {
                        videoList.value?.add(Video(cursor.getString(nameColumnIndex), cursor.getString(dataColumnIndex)))
                    } while (cursor.moveToNext())
                }
                cursor.close()
            }
        }
    }
}