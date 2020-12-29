package com.bowoon.android.android_videoview.databinding

import android.media.ThumbnailUtils
import android.os.CancellationSignal
import android.provider.MediaStore
import android.util.Size
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bowoon.android.android_videoview.R
import java.io.File

@BindingAdapter("bindImage")
fun ImageView.bindImage(path: String) {
    val size = Size(90, 90)
    val cancellationSignal = CancellationSignal()

    this.background = ContextCompat.getDrawable(this.context, R.drawable.corners_image_view)
    this.clipToOutline = true

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
        this.setImageBitmap(ThumbnailUtils.createVideoThumbnail(File(path), size, cancellationSignal))
    } else {
        this.setImageBitmap(ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MINI_KIND))
    }
}