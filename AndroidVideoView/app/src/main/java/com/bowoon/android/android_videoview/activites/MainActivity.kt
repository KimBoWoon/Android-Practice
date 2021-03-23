package com.bowoon.android.android_videoview.activites

import android.Manifest
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.android.android_videoview.R
import com.bowoon.android.android_videoview.activites.vm.MainActivityVM
import com.bowoon.android.android_videoview.adapter.FolderListAdapter
import com.bowoon.android.android_videoview.databinding.ActivityMainBinding
import com.bowoon.android.android_videoview.utils.px
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
        
        lifecycle.addObserver(viewModel)

        permissionCheck()
        initLiveData()
        viewModel.findVideoFolder(this)
    }

    private fun permissionCheck() {
        if (!hasStoragePermission()) {
            EasyPermissions.requestPermissions(this, "Storage Permission", 1000, Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (!hasWindowPermission()) {
            EasyPermissions.requestPermissions(this, "Window Permission", 1001, Manifest.permission.SYSTEM_ALERT_WINDOW)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (!hasForegroundService()) {
                EasyPermissions.requestPermissions(this, "Window Permission", 1001, Manifest.permission.FOREGROUND_SERVICE)
            }
        }
    }

    private fun initLiveData() {
        viewModel.folderMap.observe(this) {
            binding.recyclerview.adapter = FolderListAdapter(it)
            if (binding.recyclerview.itemDecorationCount == 0) {
                binding.recyclerview.addItemDecoration(object : RecyclerView.ItemDecoration() {
                    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                        val position = parent.getChildAdapterPosition(view)
                        val itemCount = state.itemCount

                        if (position in 0 .. itemCount) {
                            outRect.left = 5.px
                            outRect.right = 5.px
                            outRect.top = 5.px
                        }
                    }
                })
            }
        }
    }

    private fun hasStoragePermission(): Boolean {
        return EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun hasWindowPermission(): Boolean {
        return EasyPermissions.hasPermissions(this, Manifest.permission.SYSTEM_ALERT_WINDOW)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun hasForegroundService(): Boolean {
        return EasyPermissions.hasPermissions(this, Manifest.permission.FOREGROUND_SERVICE)
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
