package com.bowoon.android.android_videoview.activites.vm

import android.content.Context
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bowoon.android.android_videoview.model.Video

class MainActivityVM : ViewModel() {
    val folderMap = MutableLiveData<MutableMap<String, MutableList<Video>>>()

    fun findVideoFolder(context: Context) {
        context.contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                arrayOf(
                        MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME,
                        MediaStore.Video.Media.DISPLAY_NAME,
                        MediaStore.Video.Media.DATA
                ),
                null,
                null,
                null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                val bucketColumn = cursor.getColumnIndex(MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME)
                val dataColumnIndex = cursor.getColumnIndex(MediaStore.Video.Media.DATA)
                val nameColumnIndex = cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME)
                val folderMap = mutableMapOf<String, MutableList<Video>>()

                do {
                    val bucket = cursor.getString(bucketColumn)
                    val name = cursor.getString(nameColumnIndex)
                    val path = cursor.getString(dataColumnIndex)
                    if (folderMap.containsKey(bucket)) {
                        folderMap[bucket]?.add(Video(name, path))
                    } else {
                        folderMap[bucket] = mutableListOf(Video(name, path))
                    }
                } while (cursor.moveToNext())

                this.folderMap.value = folderMap
            }
        }
    }
}