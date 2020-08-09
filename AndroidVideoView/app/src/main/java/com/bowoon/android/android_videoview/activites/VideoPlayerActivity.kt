package com.bowoon.android.android_videoview.activites

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.View
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.android.logcat.log.ALog
import com.bowoon.android.android_videoview.R
import com.bowoon.android.android_videoview.databinding.VideoSurfaceviewBinding
import com.bowoon.android.android_videoview.model.Video
import com.bowoon.android.android_videoview.services.VideoService
import com.bowoon.android.android_videoview.utils.Utils
import java.io.IOException

class VideoPlayerActivity : Activity() {
    private var mediaPlayer: MediaPlayer? = null
    private var seekBarFlag = false
    private var isPlay = false
    private var video: Video? = null
    private var binding: VideoSurfaceviewBinding? = null
    
    companion object {
        const val LANDSCAPE = 1
        const val PORTRAIT = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ALog.i("VideoPlayerActivity")

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        video = intent.getParcelableExtra("videoContent") as Video

        initView()
        registerListener()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        ALog.i("onConfigurationChanged")

        resizeSurfaceView()
    }

    private fun resizeSurfaceView() {
        val displayMetrics = Utils.getDisplayMetrics(this)
        val windowManager = Utils.getWindowManager(this)

        if (displayMetrics != null && windowManager != null && binding != null) {
            binding?.let { videoSurfaceViewBinding ->
                videoSurfaceViewBinding.mainSurfaceView.let { surfaceView ->
                    if (windowManager.defaultDisplay?.rotation == LANDSCAPE) {
                        Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show()
                        surfaceView.layoutParams.apply {
                            width = displayMetrics.widthPixels
                            height = displayMetrics.heightPixels
                        }
                    } else {
                        Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show()
                        surfaceView.layoutParams.apply {
                            width = displayMetrics.widthPixels
                            mediaPlayer?.let { height = (it.videoHeight.toFloat() / it.videoWidth.toFloat() * displayMetrics.widthPixels.toFloat()).toInt() }
                        }
                    }
                }
            }
        }
    }

    private fun initView() {
        binding = DataBindingUtil.setContentView<VideoSurfaceviewBinding>(this, R.layout.video_surfaceview)
        binding?.mainSurfaceView?.holder?.addCallback(SurfaceViewCallbackClass())
        binding?.playVideoTitle?.text = video?.title
        mediaPlayer = MediaPlayer()
    }

    private fun registerListener() {
        binding?.videoSeekbar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (seekBar.max == progress) {
                    mediaPlayer?.stop()
                    finish()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                seekBarFlag = false
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                seekBarFlag = true
                val position = seekBar.progress
                mediaPlayer?.seekTo(position)
                ProgressSeekBar().start()
            }
        })

        binding?.videoService?.setOnClickListener {
            ALog.i("startService")
            startService(Intent(this, VideoService::class.java).apply {
                putExtra("video", video)
                putExtra("currentTime", mediaPlayer?.currentPosition)
            })
            releaseMediaPlayer()
            finish()
        }

        binding?.videoPlay?.setOnClickListener {
            if (!isPlay) {
                isPlay = true
                mediaPlayer?.start()
            }
        }

        binding?.videoPause?.setOnClickListener {
            if (isPlay) {
                isPlay = false
                mediaPlayer?.pause()
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            binding?.videoInformation?.visibility = View.VISIBLE
            binding?.playVideoTitle?.visibility = View.VISIBLE
//            binding?.mDialogBtn?.visibility = View.VISIBLE
            binding?.videoService?.visibility = View.VISIBLE
            binding?.videoTime?.text = mediaPlayer?.let { String.format("%s / %s", getStringTime(it.currentPosition), getStringTime(it.duration)) }

            Handler().postDelayed({
                binding?.videoInformation?.visibility = View.GONE
                binding?.playVideoTitle?.visibility = View.GONE
//                binding?.mDialogBtn?.visibility = View.GONE
                binding?.videoService?.visibility = View.GONE
            }, 5000)

            return false
        }
        return super.onTouchEvent(event)
    }

    private fun playVideo(path: String) {
        try {
            mediaPlayer?.apply {
                setDataSource(path)
                setDisplay(binding?.mainSurfaceView?.holder)
                prepare()
                start()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun releaseMediaPlayer() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        seekBarFlag = false
        mediaPlayer?.release()
    }

    override fun onPause() {
        super.onPause()
        ALog.i("onPause")
    }

    override fun onResume() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        super.onResume()
        ALog.i("onResume")
    }

    override fun onStop() {
        super.onStop()
        releaseMediaPlayer()
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

        return "$hour:$minute:$second"
    }

    private inner class ProgressSeekBar : Thread() {
        override fun run() {
            while (seekBarFlag) {
                mediaPlayer?.let { binding?.videoSeekbar?.progress = it.currentPosition }
            }
        }
    }

    private inner class SurfaceViewCallbackClass : SurfaceHolder.Callback {
        override fun surfaceCreated(holder: SurfaceHolder) {
            ALog.i("surfaceCreated")
            seekBarFlag = true
            isPlay = true
            video?.let {
                playVideo(it.path)
            }
            mediaPlayer?.let { binding?.videoSeekbar?.max = it.duration }
            ProgressSeekBar().start()
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            ALog.i("surfaceChanged")
            mediaPlayer?.setDisplay(holder)
            resizeSurfaceView()
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            ALog.i("surfaceDestroyed")
            releaseMediaPlayer()
        }
    }
}
