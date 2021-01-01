package com.bowoon.android.android_videoview.databinding

import android.media.ThumbnailUtils
import android.net.Uri
import android.os.CancellationSignal
import android.provider.MediaStore
import android.util.Size
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bowoon.android.android_videoview.R
import com.bowoon.android.android_videoview.utils.Utils
import java.io.File

@BindingAdapter("bindImage")
fun ImageView.bindImage(uri: Uri) {
    val size = Size(90, 90)
    val cancellationSignal = CancellationSignal()

    this.background = ContextCompat.getDrawable(this.context, R.drawable.corners_image_view)
    this.clipToOutline = true

    Utils.getPathFromUri(context, uri)?.let { path ->
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            this.setImageBitmap(ThumbnailUtils.createVideoThumbnail(File(path), size, cancellationSignal))
        } else {
            this.setImageBitmap(ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MINI_KIND))
        }
    }
}