package com.bowoon.android.android_videoview.activites

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bowoon.android.android_videoview.R
import com.bowoon.android.android_videoview.activites.vm.VideoPlayerActivityVM
import com.bowoon.android.android_videoview.databinding.VideoSurfaceviewBinding
import com.bowoon.android.android_videoview.model.Video
import com.bowoon.android.android_videoview.services.VideoService
import com.bowoon.android.android_videoview.utils.Utils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class VideoPlayerActivity : AppCompatActivity() {
    private val binding by lazy {
        DataBindingUtil.setContentView<VideoSurfaceviewBinding>(this, R.layout.video_surfaceview)
    }
    private val viewModel by lazy {
        ViewModelProvider(this).get(VideoPlayerActivityVM::class.java)
    }
    
    companion object {
        const val TAG = "VideoPlayerActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i(TAG, "VideoPlayerActivity")

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()

        viewModel.video.value = intent.getParcelableExtra("videoContent") as Video

        initBinding()
        initLiveData()
    }

    private fun initLiveData() {
        if (viewModel.player.value == null) {
            viewModel.player.value = MediaPlayer()
        }
        viewModel.isPlay.observe(this) {
            if (it) {
                viewModel.player.value?.start()
            } else {
                viewModel.player.value?.pause()
            }
        }
        viewModel.playTime.observe(this) { playTime ->
            viewModel.isPlay.value?.let {
                viewModel.player.value?.let { binding.videoSeekbar.progress = playTime }
            }
        }
        viewModel.video.observe(this) {
            binding.playVideoTitle.text = it.title
        }
        viewModel.orientation.observe(this) {
            resizeSurfaceView()
        }
    }

    private fun initBinding() {
        binding.mainSurfaceView.holder?.addCallback(SurfaceViewCallbackClass())
        binding.videoSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (seekBar.max == progress) {
                    viewModel.player.value?.stop()
                    finish()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                viewModel.player.value?.seekTo(seekBar.progress)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                viewModel.player.value?.seekTo(seekBar.progress)
            }
        })

        binding.videoService.setOnClickListener {
            Log.i(TAG, "startService")
            startService(Intent(this, VideoService::class.java).apply {
                putExtra("video", viewModel.video.value)
                putExtra("currentTime", viewModel.player.value?.currentPosition)
            })
            releaseMediaPlayer()
            finish()
        }

        binding.videoPlay.setOnClickListener {
            viewModel.isPlay.value = true
        }

        binding.videoPause.setOnClickListener {
            viewModel.isPlay.value = false
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.i(TAG, "onConfigurationChanged")

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show()
            viewModel.orientation.value = Unit
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show()
            viewModel.orientation.value = Unit
        }
    }

    private fun resizeSurfaceView() {
        var newWidth = viewModel.player.value?.videoWidth ?: 0
        var newHeight = viewModel.player.value?.videoHeight ?: 0
        var rate = 0.0f
        val max = if (newWidth > newHeight) {
            Utils.getDisplayMetrics(this@VideoPlayerActivity).widthPixels
        } else {
            Utils.getDisplayMetrics(this@VideoPlayerActivity).heightPixels
        }.toFloat()

        if (newWidth == 0 || newHeight == 0) {
            return
        }

        if (newWidth > newHeight) {
            rate = max / newWidth
            newHeight = (newHeight * rate).toInt()
            newWidth = max.toInt()
        } else {
            rate = max / newHeight
            newWidth = (newWidth * rate).toInt()
            newHeight = max.toInt()
        }

        if (newHeight > Utils.getDisplayMetrics(this).heightPixels) {
            newHeight = Utils.getDisplayMetrics(this).heightPixels
        }

        if (newWidth > Utils.getDisplayMetrics(this).widthPixels) {
            newWidth = Utils.getDisplayMetrics(this).widthPixels
        }

        binding.mainSurfaceView.let { surfaceView ->
            surfaceView.layoutParams.apply {
                width = newWidth
                height = newHeight
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            binding.videoInformation.visibility = View.VISIBLE
            binding.playVideoTitle.visibility = View.VISIBLE
//            binding.mDialogBtn?.visibility = View.VISIBLE
            binding.videoService.visibility = View.VISIBLE
            binding.videoTime.text = viewModel.player.value?.let { String.format("%s / %s", getStringTime(it.currentPosition), getStringTime(it.duration)) }

            Single.timer(5000, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                binding.videoInformation.visibility = View.GONE
                                binding.playVideoTitle.visibility = View.GONE
//                                binding.mDialogBtn.visibility = View.GONE
                                binding.videoService.visibility = View.GONE
                            },
                            { it.printStackTrace() }
                    )

            return false
        }
        return super.onTouchEvent(event)
    }

    private fun playVideo(path: String) {
        try {
            viewModel.player.value?.apply {
                setDataSource(path)
                setDisplay(binding.mainSurfaceView.holder)
                prepare()
                start()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun releaseMediaPlayer() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        viewModel.player.value?.release()
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause")
    }

    override fun onResume() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        super.onResume()
        Log.i(TAG, "onResume")
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseMediaPlayer()
    }

    private fun getStringTime(time: Int): String {
        val currentSecond = time / 1000
        val second = currentSecond % 60
        val minute = currentSecond / 60 % 60
        val hour = currentSecond / 3600

        return "$hour:${if (minute < 10) "0$minute" else minute}:${if (second < 10) "0${second}" else second}"
    }

    private inner class SurfaceViewCallbackClass : SurfaceHolder.Callback {
        override fun surfaceCreated(holder: SurfaceHolder) {
            Log.i(TAG, "surfaceCreated")
            viewModel.isPlay.value = true
            viewModel.video.value?.let {
                playVideo(it.path)
            }
            viewModel.player.value?.let { binding.videoSeekbar.max = it.duration }
            viewModel.orientation.value?.let { resizeSurfaceView() }
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            Log.i(TAG, "surfaceChanged")
            viewModel.player.value?.setDisplay(holder)
            viewModel.orientation.value?.let {
                resizeSurfaceView()
            }
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            Log.i(TAG, "surfaceDestroyed")
            releaseMediaPlayer()
        }
    }
}
