package com.bowoon.android.android_videoview.activites

import android.Manifest
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bowoon.android.android_videoview.R
import com.bowoon.android.android_videoview.activites.vm.MainActivityVM
import com.bowoon.android.android_videoview.adapter.VideoAdapter
import com.bowoon.android.android_videoview.databinding.ActivityMainBinding
import com.bowoon.android.android_videoview.model.Video
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }
    private val viewModel by lazy {
        ViewModelProvider(this).get(MainActivityVM::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
        initLiveData()
    }

    private fun init() {
        if (!hasStoragePermission()) {
            EasyPermissions.requestPermissions(this, "Storage Permission", 1000, Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (!hasWindowPermission()) {
            EasyPermissions.requestPermissions(this, "Window Permission", 1001, Manifest.permission.SYSTEM_ALERT_WINDOW)
        }

        viewModel.fetchAllVideos(this)
    }

    private fun initLiveData() {
        viewModel.videoList.observe(this) {
            binding.recyclerview.setHasFixedSize(true)
            binding.recyclerview.adapter = VideoAdapter(it)
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
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size)

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size)
    }

    companion object {
        const val TAG = "MainActivity"
    }
}
