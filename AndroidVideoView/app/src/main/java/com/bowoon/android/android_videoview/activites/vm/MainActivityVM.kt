package com.bowoon.android.android_videoview.activites.vm

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import androidx.lifecycle.*
import com.bowoon.android.android_videoview.model.Video

class MainActivityVM : ViewModel(), LifecycleObserver {
    val folderMap = MutableLiveData<MutableMap<String, MutableList<Video>>>()

    fun findVideoFolder(context: Context) {
        context.contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                arrayOf(
                        MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME,
                        MediaStore.Video.VideoColumns._ID,
                        MediaStore.Video.VideoColumns.DISPLAY_NAME,
                        MediaStore.Video.VideoColumns.DURATION
                ),
                null,
                null,
                null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                val bucketColumn = cursor.getColumnIndex(MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME)
                val nameColumnIndex = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DISPLAY_NAME)
                val durationColumnIndex = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DURATION)
                val idColumnIndex = cursor.getColumnIndex(MediaStore.Video.VideoColumns._ID)
                val folderMap = mutableMapOf<String, MutableList<Video>>()

                do {
                    val bucket = cursor.getString(bucketColumn)
                    val name = cursor.getString(nameColumnIndex)
                    val duration = cursor.getString(durationColumnIndex)
                    val id = cursor.getLong(idColumnIndex)
                    val contentUris = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)

                    if (folderMap.containsKey(bucket)) {
                        folderMap[bucket]?.add(Video(name, contentUris, duration))
                    } else {
                        folderMap[bucket] = mutableListOf(Video(name, contentUris, duration))
                    }
                } while (cursor.moveToNext())

                this.folderMap.value = folderMap
            }
        }
    }
}