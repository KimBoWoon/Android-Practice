package com.bowoon.android.android_videoview

import android.Manifest
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.android.logcat.log.ALog
import com.bowoon.android.android_videoview.adapter.RecyclerAdapter
import com.bowoon.android.android_videoview.vo.Item
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.*


class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    private lateinit var videoList: ArrayList<Item>
    private lateinit var recyclerView: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
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
        videoList = ArrayList()
        videoList = fetchAllVideos()

        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = RecyclerAdapter(applicationContext, videoList)
    }

    private fun fetchAllVideos(): ArrayList<Item> {
        val projection = arrayOf(MediaStore.Video.Media.DATA, MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.DATE_ADDED)

        val videoCursor = applicationContext.contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                null, null,
                "date_added DESC")

        val result = ArrayList<Item>()
        val dataColumnIndex = videoCursor!!.getColumnIndex(projection[0])
        val nameColumnIndex = videoCursor.getColumnIndex(projection[1])

        if (videoCursor.moveToFirst()) {
            do {
                result.add(Item(videoCursor.getString(nameColumnIndex), videoCursor.getString(dataColumnIndex)))
            } while (videoCursor.moveToNext())
        }
        videoCursor.close()
        return result
    }

    private fun hasStoragePermission(): Boolean {
        return EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun hasWindowPermission(): Boolean {
        return EasyPermissions.hasPermissions(this, Manifest.permission.SYSTEM_ALERT_WINDOW)
    }

//    private fun requestReadExternalStoragePermission() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
//
//                // Show an expanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//
//            } else {
//
//                // No explanation needed, we can request the permission.
//                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
//                        MY_PERMISSIONS_REQUEST_READ_EXT_STORAGE)
//                // MY_PERMISSIONS_REQUEST_READ_EXT_STORAGE is an
//                // app-defined int constant. The callback method gets the
//                // result of the request.
//            }
//        }
//    }

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
