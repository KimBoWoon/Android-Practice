package com.bowoon.android.android_videoview.video

import android.annotation.TargetApi
import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.graphics.Point
import android.media.MediaPlayer
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.DisplayMetrics
import android.view.*
import android.widget.Button
import android.widget.Toast
import com.android.logcat.log.ALog
import com.bowoon.android.android_videoview.R
import com.bowoon.android.android_videoview.vo.Item
import java.io.IOException

class VideoService : Service(), SurfaceHolder.Callback {
    private var currentTime: Int = 0
    private lateinit var mView: View
    private lateinit var mManager: WindowManager
    private lateinit var mParams: WindowManager.LayoutParams
    private var mTouchX: Float = 0.toFloat()
    private var mTouchY: Float = 0.toFloat()
    private var mViewX: Int = 0
    private var mViewY: Int = 0
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var surfaceHolder: SurfaceHolder
    private lateinit var surfaceView: SurfaceView
    private lateinit var path: String
    private lateinit var exitBtn: Button
    private lateinit var playBtn: Button
    private lateinit var pauseBtn: Button
    private lateinit var intent: Intent
    private lateinit var displayMetrics: DisplayMetrics
    private var isPause: Boolean = false
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private lateinit var gestureDetector: GestureDetector
    private val MIN_WIDTH = 540
    private val MIN_HEIGHT = 304

    private var listener: View.OnClickListener = View.OnClickListener { v ->
        when (v.id) {
            R.id.service_play -> if (isPause) {
                isPause = false
                mediaPlayer.start()
            }
            R.id.service_pause -> if (mediaPlayer.isPlaying) {
                isPause = true
                mediaPlayer.pause()
            }
            R.id.service_exit -> {
                releaseMediaPlayer()
                stopForeground(true)
                stopService(intent)
            }
        }
    }

    private val mViewTouchListener = View.OnTouchListener { _, event ->
        val count = event.pointerCount

        if (count == 1 && event.action == MotionEvent.ACTION_MOVE) {
            val x = (mTouchX - event.rawX).toInt()
            val y = (mTouchY - event.rawY).toInt()

            mParams.x = mViewX + x
            mParams.y = mViewY + y

            mManager.updateViewLayout(mView, mParams)

            return@OnTouchListener true
        }

        gestureDetector.onTouchEvent(event)
        scaleGestureDetector.onTouchEvent(event)
        true
    }

    override fun onCreate() {
        super.onCreate()

        initView()
    }

    private fun initView() {
        val mInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mView = mInflater.inflate(R.layout.service_layout, null)

        scaleGestureDetector = ScaleGestureDetector(applicationContext, ServiceGestureDetector())
        gestureDetector = GestureDetector(applicationContext, CustomGestureDetector())

        mView.setOnTouchListener(mViewTouchListener)

        surfaceView = mView.findViewById<View>(R.id.service_layout_video) as SurfaceView
        playBtn = mView.findViewById<View>(R.id.service_play) as Button
        pauseBtn = mView.findViewById<View>(R.id.service_pause) as Button
        exitBtn = mView.findViewById<View>(R.id.service_exit) as Button
        playBtn.setOnClickListener(listener)
        pauseBtn.setOnClickListener(listener)
        exitBtn.setOnClickListener(listener)

        surfaceHolder = surfaceView.holder
        surfaceHolder.addCallback(this)

        mediaPlayer = MediaPlayer()

        mParams = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                    PixelFormat.TRANSLUCENT)
        } else {
            WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                    PixelFormat.TRANSLUCENT)
        }
        mParams.gravity = Gravity.BOTTOM or Gravity.END

        displayMetrics = applicationContext.resources.displayMetrics

        mParams.width = MIN_WIDTH
        mParams.height = MIN_HEIGHT

        mManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        mManager.addView(mView, mParams)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        ALog.i("onStartCommand")
        this.intent = intent
        currentTime = intent.getIntExtra("currentTime", -1)
        ALog.i(currentTime)
        val item = intent.getSerializableExtra("video") as Item
        path = item.path
        startForeground(startId, Notification())

        return super.onStartCommand(intent, flags, startId)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        ALog.i("surfaceCreated")
        playVideo(path)
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        ALog.i("surfaceChanged")
        holder.setFixedSize(width, height)
        mediaPlayer.setDisplay(holder)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        ALog.i("surfaceDestroyed")
    }

    private fun playVideo(path: String?) {
        try {
            mediaPlayer.setDataSource(path)
            mediaPlayer.setDisplay(surfaceHolder)
            mediaPlayer.prepare()
            if (currentTime != -1) {
                ALog.i(currentTime)
                mediaPlayer.seekTo(currentTime.toInt())
            }
            mediaPlayer.start()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    @TargetApi(Build.VERSION_CODES.N)
    override fun onDestroy() {
        super.onDestroy()
        ALog.i("onDestroy")
        stopSelf()
        stopForeground(true)
        mManager.removeView(mView)
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        ALog.i("onTaskRemoved")
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun releaseMediaPlayer() {
        mediaPlayer.release()
    }

    private inner class CustomGestureDetector : GestureDetector.SimpleOnGestureListener() {

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            playBtn.visibility = View.VISIBLE
            pauseBtn.visibility = View.VISIBLE
            exitBtn.visibility = View.VISIBLE
            Handler().postDelayed({
                playBtn.visibility = View.GONE
                pauseBtn.visibility = View.GONE
                exitBtn.visibility = View.GONE
            }, 3000)

            return false
        }

        override fun onDown(e: MotionEvent): Boolean {
            mTouchX = e.rawX
            mTouchY = e.rawY
            mViewX = mParams.x
            mViewY = mParams.y

            return false
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            val screenDivision = mParams.width / 2

            if (e.x > screenDivision) {
                Toast.makeText(applicationContext, "10초 앞으로", Toast.LENGTH_SHORT).show()
                mediaPlayer.seekTo(mediaPlayer.currentPosition + 10000)
            } else {
                Toast.makeText(applicationContext, "10초 뒤로", Toast.LENGTH_SHORT).show()
                mediaPlayer.seekTo(mediaPlayer.currentPosition - 10000)
            }

            return false
        }
    }

    private inner class ServiceGestureDetector : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        private var mW: Int = 0
        private var mH: Int = 0

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            ALog.d("onScale")
            mW = (mW.toDouble() * detector.scaleFactor.toDouble()).toInt()
            mH = (mH.toDouble() * detector.scaleFactor.toDouble()).toInt()
            ALog.d("ScaleFactor = " + detector.scaleFactor.toInt())
            if (mW <= MIN_WIDTH) {
                mW = MIN_WIDTH
                mH = MIN_HEIGHT
            } else if (mW >= displayMetrics.widthPixels) {
                mW = displayMetrics.widthPixels
                mH = computeRatio(16, 9, displayMetrics).y
            }
            ALog.d("scale=" + detector.scaleFactor + ", w=" + mW + ", h=" + mH)
            surfaceHolder.setFixedSize(mW, mH)
            mParams.width = mW
            mParams.height = mH
            ALog.i(mParams.width)
            ALog.i(mParams.height)
            mManager.updateViewLayout(mView, mParams)
            return true
        }

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            ALog.d("onScaleBegin")
            mW = mParams.width
            mH = mParams.height
            ALog.d("scale=" + detector.scaleFactor + ", w=" + mW + ", h=" + mH)
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector) {
            ALog.d("onScaleEnd")
            ALog.d("scale=" + detector.scaleFactor + ", w=" + mW + ", h=" + mH)
        }

        fun computeRatio(widthRatio: Int, heightRatio: Int, displayMetrics: DisplayMetrics): Point {
            var width = displayMetrics.widthPixels
            var height = displayMetrics.heightPixels
            val point = screenRatio(displayMetrics)
            val result = Point()

            if (displayMetrics.widthPixels * widthRatio > displayMetrics.heightPixels * heightRatio) {
                width = displayMetrics.heightPixels * point.y / point.x
            } else {
                height = displayMetrics.widthPixels * point.x / point.y
            }

            result.x = width
            result.y = height

            ALog.i(result.x)
            ALog.i(result.y)

            return result
        }

        private fun screenRatio(displayMetrics: DisplayMetrics): Point {
            val max: Int
            val min: Int
            val gcd: Int
            val widthPixels: Int = displayMetrics.widthPixels
            val heightPixels: Int = displayMetrics.heightPixels
            val result = Point()

            if (widthPixels < heightPixels) {
                max = widthPixels
                min = heightPixels
            } else {
                max = heightPixels
                min = widthPixels
            }

            gcd = getGCD(max, min)

            result.x = widthPixels / gcd
            result.y = heightPixels / gcd

            return result
        }

        fun getGCD(x: Int, y: Int): Int {
            var a = x
            var b = y
            while (b != 0) {
                val temp = a % b
                a = b
                b = temp
            }
            return Math.abs(a)
        }
    }
}
