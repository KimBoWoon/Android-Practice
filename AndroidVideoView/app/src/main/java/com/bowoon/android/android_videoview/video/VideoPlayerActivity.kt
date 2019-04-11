package com.bowoon.android.android_videoview.video

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.*
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.android.logcat.log.ALog
import com.bowoon.android.android_videoview.R
import com.bowoon.android.android_videoview.databinding.VideoSurfaceviewBinding
import com.bowoon.android.android_videoview.vo.Item
import java.io.IOException

class VideoPlayerActivity : Activity(), SurfaceHolder.Callback {
//    private lateinit var mSurfaceHolder: SurfaceHolder
    private lateinit var mMediaPlayer: MediaPlayer
    private var mSeekBarFlag: Boolean = false
    private var isPlay: Boolean = false
    private lateinit var mVideoItem: Item
    private lateinit var mDisplayMetrics: DisplayMetrics
    private var mStartTime: Long = 0
    private var mEndTime: Long = 0
    private var mFps: Int = 0

    private lateinit var binding: VideoSurfaceviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ALog.i("VideoPlayerActivity")

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val intent = intent
        mVideoItem = intent.getSerializableExtra("videoContent") as Item

        initView()
        registerListener()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        ALog.i("onConfigurationChanged")

        arrangeVideo()
    }

    private fun initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.video_surfaceview)

        mMediaPlayer = MediaPlayer()
        binding.mainSurfaceview.holder.addCallback(this)
        mDisplayMetrics = applicationContext.resources.displayMetrics

        binding.playVideoTitle.text = mVideoItem.title
    }

    private fun registerListener() {
        binding.videoSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
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

        binding.videoService.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                ALog.i("startService")
                val serviceIntent = Intent(applicationContext, VideoService::class.java)
                serviceIntent.putExtra("video", mVideoItem)
                serviceIntent.putExtra("currentTime", mMediaPlayer.currentPosition)
                ALog.i(mMediaPlayer.currentPosition)
                ALog.i(serviceIntent.getIntExtra("currentTime", -1))
                startService(serviceIntent)
                finish()
            }
        })

        binding.videoPlay.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                if (!isPlay) {
                    isPlay = true
                    mMediaPlayer.start()
                }
            }
        })

        binding.videoPause.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                if (isPlay) {
                    isPlay = false
                    mMediaPlayer.pause()
                }
            }
        })
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            binding.videoInformation.visibility = View.VISIBLE
            binding.playVideoTitle.visibility = View.VISIBLE
//            mDialogBtn.visibility = View.VISIBLE
            binding.videoService.visibility = View.VISIBLE
            binding.videoTime.text = getStringTime(mMediaPlayer.currentPosition) + " / " + getStringTime(mMediaPlayer.duration)

            Handler().postDelayed({
                binding.videoInformation.visibility = View.GONE
                binding.playVideoTitle.visibility = View.GONE
//                mDialogBtn.visibility = View.GONE
                binding.videoService.visibility = View.GONE
            }, 5000)

            return false
        }
        return super.onTouchEvent(event)
    }

    // 영상 비율에 맞게 확대 / 축소
    private fun arrangeVideo() {
        val videoWidth = mMediaPlayer.videoWidth
        val videoHeight = mMediaPlayer.videoHeight

        val screenWidth = mDisplayMetrics.widthPixels
        val screenHeight = mDisplayMetrics.heightPixels

        val lp = binding.mainSurfaceview.layoutParams

        if (screenWidth < screenHeight) {
            lp.width = screenWidth
            lp.height = (videoHeight.toFloat() / videoWidth.toFloat() * screenWidth.toFloat()).toInt()
        } else {
            lp.width = (videoWidth.toFloat() / videoHeight.toFloat() * screenHeight.toFloat()).toInt()
            lp.height = screenHeight
        }

        binding.mainSurfaceview.layoutParams = lp
    }

    private fun playVideo(path: String) {
        try {
            mMediaPlayer.setDataSource(path)
            mMediaPlayer.setDisplay(binding.mainSurfaceview.holder)
            mMediaPlayer.prepare()
            mMediaPlayer.start()
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

    override fun surfaceCreated(holder: SurfaceHolder) {
        ALog.i("surfaceCreated")
        mSeekBarFlag = true
        isPlay = true
        playVideo(mVideoItem.path)
        binding.videoSeekbar.max = mMediaPlayer.duration
        ProgressSeekBar().start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        ALog.i("surfaceChanged")
        mMediaPlayer.setDisplay(holder)
        arrangeVideo()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        ALog.i("surfaceDestroyed")
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
                binding.videoSeekbar.progress = mMediaPlayer.currentPosition
            }
        }
    }
}
