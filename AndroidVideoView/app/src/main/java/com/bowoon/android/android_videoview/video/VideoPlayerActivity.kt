package com.bowoon.android.android_videoview.video

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
import com.android.logcat.log.ALog
import com.bowoon.android.android_videoview.R
import com.bowoon.android.android_videoview.model.Video
import kotlinx.android.synthetic.main.video_surfaceview.*
import java.io.IOException

class VideoPlayerActivity : Activity() {
    private var mMediaPlayer: MediaPlayer = MediaPlayer()
    private var mSeekBarFlag = false
    private var isPlay = false
    private lateinit var mVideoVideo: Video
    
    companion object {
        const val LANDSCAPE = 1
        const val PORTRAIT = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_surfaceview)

        ALog.i("VideoPlayerActivity")

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        mVideoVideo = intent.getSerializableExtra("videoContent") as Video

        initView()
        registerListener()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        ALog.i("onConfigurationChanged")

        resizeSurfaceView()
    }

    private fun resizeSurfaceView() {
        val mDisplayMetrics = applicationContext.resources.displayMetrics

        if (windowManager.defaultDisplay.rotation == LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show()
            mainSurfaceView.layoutParams.apply {
                width = mDisplayMetrics.widthPixels
                height = mDisplayMetrics.heightPixels
            }
        } else if (windowManager.defaultDisplay.rotation == PORTRAIT) {
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show()
            mainSurfaceView.layoutParams.apply {
                width = mDisplayMetrics.widthPixels
                height = (mMediaPlayer.videoHeight.toFloat() / mMediaPlayer.videoWidth.toFloat() * mDisplayMetrics.widthPixels.toFloat()).toInt()
            }
        }
    }

    private fun initView() {
        mainSurfaceView.holder.addCallback(SurfaceViewCallbackClass())
        playVideoTitle.text = mVideoVideo.title
    }

    private fun registerListener() {
        videoSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (seekBar.max == progress) {
                    mMediaPlayer.stop()
                    finish()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                mSeekBarFlag = false
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                mSeekBarFlag = true
                val position = seekBar.progress
                mMediaPlayer.seekTo(position)
                ProgressSeekBar().start()
            }
        })

        videoService.setOnClickListener {
            ALog.i("startService")
            startService(Intent(applicationContext, VideoService::class.java).apply {
                putExtra("video", mVideoVideo)
                putExtra("currentTime", mMediaPlayer.currentPosition)
            })
            finish()
        }

        videoPlay.setOnClickListener {
            if (!isPlay) {
                isPlay = true
                mMediaPlayer.start()
            }
        }

        videoPause.setOnClickListener {
            if (isPlay) {
                isPlay = false
                mMediaPlayer.pause()
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            videoInformation.visibility = View.VISIBLE
            playVideoTitle.visibility = View.VISIBLE
//            mDialogBtn.visibility = View.VISIBLE
            videoService.visibility = View.VISIBLE
            videoTime.text = String.format("%s / %s", getStringTime(mMediaPlayer.currentPosition), getStringTime(mMediaPlayer.duration))

            Handler().postDelayed({
                videoInformation.visibility = View.GONE
                playVideoTitle.visibility = View.GONE
//                mDialogBtn.visibility = View.GONE
                videoService.visibility = View.GONE
            }, 5000)

            return false
        }
        return super.onTouchEvent(event)
    }

    private fun playVideo(path: String) {
        try {
            mMediaPlayer.apply {
                setDataSource(path)
                setDisplay(mainSurfaceView.holder)
                prepare()
                start()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun releaseMediaPlayer() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        mSeekBarFlag = false
        mMediaPlayer.release()
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
        releaseMediaPlayer()
        super.onStop()
    }

    override fun onDestroy() {
        releaseMediaPlayer()
        super.onDestroy()
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
            while (mSeekBarFlag) {
                videoSeekbar.progress = mMediaPlayer.currentPosition
            }
        }
    }

    private inner class SurfaceViewCallbackClass : SurfaceHolder.Callback {
        override fun surfaceCreated(holder: SurfaceHolder) {
            ALog.i("surfaceCreated")
            mSeekBarFlag = true
            isPlay = true
            playVideo(mVideoVideo.path)
            videoSeekbar.max = mMediaPlayer.duration
            ProgressSeekBar().start()
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            ALog.i("surfaceChanged")
            mMediaPlayer.setDisplay(holder)
            resizeSurfaceView()
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            ALog.i("surfaceDestroyed")
        }
    }
}
