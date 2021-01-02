package com.bowoon.android.android_videoview.activites

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.media.MediaPlayer
import android.net.Uri
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

class VideoPlayerActivity : AppCompatActivity() {
    private val player = MediaPlayer()
    private var hideMenu = false
    private var videoTimeThread: VideoTimeThread? = null
    private val binding by lazy {
        DataBindingUtil.setContentView<VideoSurfaceviewBinding>(this, R.layout.video_surfaceview)
    }
    private val viewModel by lazy {
        ViewModelProvider(this).get(VideoPlayerActivityVM::class.java)
    }
    private val gestureDetector by lazy {
        GestureDetector(this, CustomGestureDetector())
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

        initLiveData()
        initBinding()
    }

    private fun initLiveData() {
        viewModel.isPlay.observe(this) {
            if (it) {
                player.start()
            } else {
                player.pause()
            }
        }
        viewModel.seekTime.observe(this) { playTime ->
            viewModel.isPlay.value?.let {
                if (it) {
                    binding.videoSeekbar.progress = playTime
                    binding.videoTime.text = String.format("%s / %s", Utils.getTimeString(player.currentPosition), Utils.getTimeString(player.duration))
                }
            }
        }
        viewModel.video.observe(this) {
            binding.playVideoTitle.text = it.title
        }
        viewModel.resize.observe(this) {
            resizeSurfaceView()
        }
        viewModel.playTime.observe(this) {
            if (!this@VideoPlayerActivity.isFinishing) {
                binding.videoSeekbar.progress = it
                binding.videoTime.text = String.format("%s / %s", Utils.getTimeString(player.currentPosition), Utils.getTimeString(player.duration))
            }
        }
        viewModel.seekBarFlag.observe(this) {
            if (it && videoTimeThread == null) {
                videoTimeThread = VideoTimeThread()
                videoTimeThread?.start()
            } else {
                videoTimeThread?.interrupt()
                videoTimeThread = null
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initBinding() {
        binding.mainSurfaceView.holder?.addCallback(SurfaceViewCallbackClass())
        binding.root.setOnTouchListener { view, motionEvent ->
            gestureDetector.onTouchEvent(motionEvent)
            true
        }
        binding.videoSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (seekBar.max == progress) {
                    player.stop()
                    finish()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                viewModel.seekBarFlag.value = false
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                if (!this@VideoPlayerActivity.isFinishing) {
                    viewModel.seekBarFlag.value = true
                    player.seekTo(seekBar.progress)
                    viewModel.seekTime.value = seekBar.progress
                }
            }
        })

        binding.videoService.setOnClickListener {
            Log.i(TAG, "startService")
            startService(Intent(this, VideoService::class.java).apply {
                putExtra("video", viewModel.video.value)
                putExtra("currentTime", player.currentPosition)
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
            viewModel.resize.value = Unit
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show()
            viewModel.resize.value = Unit
        }
    }

    private fun resizeSurfaceView() {
        var newWidth = player.videoWidth
        var newHeight = player.videoHeight
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

    private fun playVideo(path: Uri) {
        try {
            player.setOnPreparedListener {
                it.start()
                viewModel.seekBarFlag.value = true
            }
            player.setOnErrorListener { mediaPlayer, what, extra ->
                when (extra) {
                    MediaPlayer.MEDIA_ERROR_UNKNOWN -> {
                        Toast.makeText(this, "MEDIA_ERROR_UNKNOWN", Toast.LENGTH_SHORT).show()
                        releaseMediaPlayer()
                        finish()
                    }
                    MediaPlayer.MEDIA_ERROR_IO -> {
                        Toast.makeText(this, "MEDIA_ERROR_IO", Toast.LENGTH_SHORT).show()
                        releaseMediaPlayer()
                        finish()
                    }
                }
                return@setOnErrorListener true
            }
            player.setDataSource(this, path)
            player.setDisplay(binding.mainSurfaceView.holder)
            player.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun releaseMediaPlayer() {
        viewModel.seekBarFlag.value = false
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        player.release()
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

    override fun onBackPressed() {
        super.onBackPressed()
        releaseMediaPlayer()
    }

    private inner class VideoTimeThread : Thread() {
        override fun run() {
            while (!isInterrupted) {
                if (!this@VideoPlayerActivity.isFinishing) {
                    viewModel.playTime.postValue(player.currentPosition)
                } else {
                    return
                }
            }
        }
    }

    private inner class CustomGestureDetector : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            if (e.action == MotionEvent.ACTION_UP) {
                binding.videoInformationGroup.visibility = View.VISIBLE
                binding.videoButtonGroup.visibility = View.VISIBLE
                binding.playVideoTitle.visibility = View.VISIBLE
//            binding.mDialogBtn.visibility = View.VISIBLE
                binding.videoService.visibility = View.VISIBLE
                binding.videoTime.text = player.let { String.format("%s / %s", Utils.getTimeString(it.currentPosition), Utils.getTimeString(it.duration)) }

                if (!hideMenu) {
                    hideMenu = true
                    Single.timer(5000, TimeUnit.MILLISECONDS)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    {
                                        binding.videoInformationGroup.visibility = View.GONE
                                        binding.videoButtonGroup.visibility = View.GONE
                                        binding.playVideoTitle.visibility = View.GONE
//                                binding.mDialogBtn.visibility = View.GONE
                                        binding.videoService.visibility = View.GONE
                                        hideMenu = false
                                    },
                                    { it.printStackTrace() }
                            )
                }

                return false
            }

            return super.onSingleTapUp(e)
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            val screenDivision = Utils.getDisplayMetrics(this@VideoPlayerActivity).widthPixels / 2

            if (e.x > screenDivision) {
                Toast.makeText(this@VideoPlayerActivity, "10초 앞으로", Toast.LENGTH_SHORT).show()
                player.seekTo(player.currentPosition + 10000)
            } else {
                Toast.makeText(this@VideoPlayerActivity, "10초 뒤로", Toast.LENGTH_SHORT).show()
                player.seekTo(player.currentPosition - 10000)
            }
            return false
        }
    }

    private inner class SurfaceViewCallbackClass : SurfaceHolder.Callback {
        override fun surfaceCreated(holder: SurfaceHolder) {
            Log.i(TAG, "surfaceCreated")
            viewModel.isPlay.value = true
            viewModel.video.value?.let {
                it.uri?.let { uri ->
                    playVideo(uri)
                }
            }
            binding.videoSeekbar.max = player.duration
            viewModel.resize.value = Unit
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            Log.i(TAG, "surfaceChanged")
            player.setDisplay(holder)
            viewModel.resize.value = Unit
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            Log.i(TAG, "surfaceDestroyed")
            releaseMediaPlayer()
        }
    }
}
