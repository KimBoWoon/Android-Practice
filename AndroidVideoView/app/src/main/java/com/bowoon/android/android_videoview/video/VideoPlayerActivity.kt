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
import com.android.logcat.log.ALog
import com.bowoon.android.android_videoview.R
import com.bowoon.android.android_videoview.vo.Item
import java.io.IOException

class VideoPlayerActivity : Activity(), SurfaceHolder.Callback {
    private lateinit var mSurfaceView: SurfaceView
    private lateinit var mSurfaceHolder: SurfaceHolder
    private lateinit var mMediaPlayer: MediaPlayer
    private lateinit var mVideoInfoView: RelativeLayout
    private lateinit var mVideoTitle: TextView
    private lateinit var mVideoTime: TextView
    private lateinit var mVideoTimeSeekBar: SeekBar
    private var mSeekBarFlag: Boolean = false
    private var isPlay: Boolean = false
    private lateinit var mVideoItem: Item
    private lateinit var mDisplayMetrics: DisplayMetrics
    private lateinit var mPlayBtn: Button
    private lateinit var mPauseBtn: Button
    private lateinit var mDialogBtn: Button
    private lateinit var mServiceBtn: Button
    private var mStartTime: Long = 0
    private var mEndTime: Long = 0
    private var mFps: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_surfaceview)

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
        mMediaPlayer = MediaPlayer()
        mVideoInfoView = findViewById<RelativeLayout>(R.id.video_information)
        mVideoTitle = findViewById<TextView>(R.id.play_video_title)
        mVideoTime = findViewById<TextView>(R.id.video_time)
        mSurfaceView = findViewById<SurfaceView>(R.id.main_surfaceview)
        mVideoTimeSeekBar = findViewById<SeekBar>(R.id.video_seekbar)
        mPlayBtn = findViewById<Button>(R.id.video_play)
        mPauseBtn = findViewById<Button>(R.id.video_pause)
        mDialogBtn = findViewById<Button>(R.id.make_gif)
        mServiceBtn = findViewById<Button>(R.id.video_service)
        mSurfaceHolder = mSurfaceView.holder
        mSurfaceHolder.addCallback(this)
        mDisplayMetrics = applicationContext.resources.displayMetrics

        mVideoTitle.text = mVideoItem.title
    }

    private fun registerListener() {
        mVideoTimeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
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

        mServiceBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                ALog.i("startService")
                val serviceIntent = Intent(applicationContext, VideoService::class.java)
                serviceIntent.putExtra("video", mVideoItem)
                serviceIntent.putExtra("currentTime", mMediaPlayer.currentPosition)
                ALog.i(mMediaPlayer.currentPosition)
                startService(serviceIntent)
                finish()
            }
        })

        mPlayBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                if (!isPlay) {
                    isPlay = true
                    mMediaPlayer.start()
                }
            }
        })

        mPauseBtn.setOnClickListener(object : View.OnClickListener {
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
            mVideoInfoView.visibility = View.VISIBLE
            mVideoTitle.visibility = View.VISIBLE
//            mDialogBtn.visibility = View.VISIBLE
            mServiceBtn.visibility = View.VISIBLE
            mVideoTime.text = getStringTime(mMediaPlayer.currentPosition) + " / " + getStringTime(mMediaPlayer.duration)

            Handler().postDelayed({
                mVideoInfoView.visibility = View.GONE
                mVideoTitle.visibility = View.GONE
//                mDialogBtn.visibility = View.GONE
                mServiceBtn.visibility = View.GONE
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

        val lp = mSurfaceView.layoutParams

        if (screenWidth < screenHeight) {
            lp.width = screenWidth
            lp.height = (videoHeight.toFloat() / videoWidth.toFloat() * screenWidth.toFloat()).toInt()
        } else {
            lp.width = (videoWidth.toFloat() / videoHeight.toFloat() * screenHeight.toFloat()).toInt()
            lp.height = screenHeight
        }

        mSurfaceView.layoutParams = lp
    }

    private fun playVideo(path: String) {
        try {
            mMediaPlayer.setDataSource(path)
            mMediaPlayer.setDisplay(mSurfaceHolder)
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
        mVideoTimeSeekBar.max = mMediaPlayer.duration
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

        return hour.toString() + ":" + minute + ":" + second
    }

    private inner class ProgressSeekBar : Thread() {
        override fun run() {
            while (mSeekBarFlag) {
                mVideoTimeSeekBar.progress = mMediaPlayer.currentPosition
            }
        }
    }
}
