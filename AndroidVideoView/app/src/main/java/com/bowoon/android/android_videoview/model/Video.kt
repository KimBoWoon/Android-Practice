package com.bowoon.android.android_videoview.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

data class Video(val title: String, val uri: Uri?, val duration: String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readParcelable<Uri>(Uri::class.java.classLoader),
            parcel.readString() ?: ""
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(title)
        dest?.writeParcelable(uri, flags)
        dest?.writeString(duration)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Video> {
        override fun createFromParcel(parcel: Parcel): Video {
            return Video(parcel)
        }

        override fun newArray(size: Int): Array<Video?> {
            return arrayOfNulls(size)
        }
    }
}
