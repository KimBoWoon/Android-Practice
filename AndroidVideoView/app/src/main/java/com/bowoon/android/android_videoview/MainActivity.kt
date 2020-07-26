package com.bowoon.android.android_videoview

import android.Manifest
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.logcat.log.ALog
import com.bowoon.android.android_videoview.listener.ItemClickListener
import com.bowoon.android.android_videoview.adapter.VideoAdapter
import com.bowoon.android.android_videoview.video.VideoPlayerActivity
import com.bowoon.android.android_videoview.model.Video
import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions


class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    private val MY_PERMISSIONS_REQUEST_READ_EXT_STORAGE = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        requestReadExternalStoragePermission()
        if (hasStoragePermission()) {
            ALog.i("Storage Permission Granted")
        } else {
            EasyPermissions.requestPermissions(this, "Storage Permission", 1000, Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (hasWindowPermission()) {
            ALog.i("Window Permission Granted")
        } else {
            EasyPermissions.requestPermissions(this, "Window Permission", 1001, Manifest.permission.SYSTEM_ALERT_WINDOW)
        }

        ALog.logSetting(applicationContext, true, false)
        ALog.setDebug(true)

        initView()
    }

    private fun initView() {
        recyclerview.setHasFixedSize(true)
        recyclerview.adapter = VideoAdapter(listener).apply {
            setItems(fetchAllVideos())
        }
        recyclerview.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    private fun fetchAllVideos(): ArrayList<Video> {
        var videoCursor: Cursor?
        var result: ArrayList<Video>
        var dataColumnIndex: Int
        var nameColumnIndex: Int

        arrayOf(MediaStore.Video.Media.DATA, MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.DATE_ADDED).let {
            videoCursor = applicationContext.contentResolver.query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    it,
                    null, null,
                    "date_added DESC")

            result = ArrayList<Video>()
            dataColumnIndex = videoCursor!!.getColumnIndex(it[0])
            nameColumnIndex = videoCursor!!.getColumnIndex(it[1])
        }

        if (videoCursor!!.moveToFirst()) {
            do {
                result.add(Video(videoCursor!!.getString(nameColumnIndex), videoCursor!!.getString(dataColumnIndex)))
            } while (videoCursor!!.moveToNext())
        }
        videoCursor!!.close()
        return result
    }

    private val listener: ItemClickListener = object : ItemClickListener {
        override fun onItemClick(video: Video) {
            Toast.makeText(applicationContext, video.title, Toast.LENGTH_SHORT).show()
            val intent = Intent(applicationContext, VideoPlayerActivity::class.java)
            intent.putExtra("videoContent", video)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            applicationContext.startActivity(intent)
        }
    }

    private fun hasStoragePermission(): Boolean {
        return EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun hasWindowPermission(): Boolean {
        return EasyPermissions.hasPermissions(this, Manifest.permission.SYSTEM_ALERT_WINDOW)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        ALog.d("onPermissionsDenied:" + requestCode + ":" + perms.size)

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        ALog.d("onPermissionsGranted:" + requestCode + ":" + perms.size)
    }
}
