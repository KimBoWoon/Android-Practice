package com.bowoon.android.android_videoview.activites.vm

import android.content.Context
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bowoon.android.android_videoview.model.Video

class MainActivityVM : ViewModel() {
    val videoList = MutableLiveData<MutableList<Video>>(mutableListOf())
    val folderList = MutableLiveData<MutableList<String>>()

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
            )?.use { cursor ->
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

    fun findVideoFolder(context: Context) {
        context.contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                arrayOf("DISTINCT ${MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME}"),
                null,
                null,
                null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                val bucketColumn = cursor.getColumnIndex(MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME)
                val folderList = mutableListOf<String>()

                do {
                    val bucket = cursor.getString(bucketColumn)
                    folderList.add(bucket)
                } while (cursor.moveToNext())

                this.folderList.value = folderList
            }
        }
    }
}