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
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.android.logcat.log.ALog
import com.bowoon.android.android_videoview.R
import com.bowoon.android.android_videoview.VideoButtonClickListener
import com.bowoon.android.android_videoview.databinding.ServiceLayoutBinding
import com.bowoon.android.android_videoview.vo.Item
import java.io.IOException

class VideoService : Service(), SurfaceHolder.Callback {
    private var currentTime: Int = 0
    private lateinit var mManager: WindowManager
    private lateinit var mParams: WindowManager.LayoutParams
    private var mTouchX: Float = 0f
    private var mTouchY: Float = 0f
    private var mViewX: Int = 0
    private var mViewY: Int = 0
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var path: String
    private lateinit var intent: Intent
    private lateinit var displayMetrics: DisplayMetrics
    private var isPause: Boolean = false
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private lateinit var gestureDetector: GestureDetector
    private val MIN_WIDTH = 540
    private val MIN_HEIGHT = 304
    private lateinit var binding: ServiceLayoutBinding

    private val play: VideoButtonClickListener = object : VideoButtonClickListener {
        override fun onClick() {
            isPause = false
            mediaPlayer.start()
        }
    }

    private val pause: VideoButtonClickListener = object : VideoButtonClickListener {
        override fun onClick() {
            isPause = true
            mediaPlayer.pause()
        }
    }

    private val exit: VideoButtonClickListener = object : VideoButtonClickListener {
        override fun onClick() {
            releaseMediaPlayer()
            stopForeground(true)
            stopService(intent)
        }
    }

    private val mViewTouchListener = View.OnTouchListener { _, event ->
        val count = event.pointerCount

        if (count == 1 && event.action == MotionEvent.ACTION_MOVE) {
            val x = (mTouchX - event.rawX).toInt()
            val y = (mTouchY - event.rawY).toInt()

            mParams.x = mViewX + x
            mParams.y = mViewY + y

            mManager.updateViewLayout(binding.root, mParams)

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

        binding = DataBindingUtil.inflate(
                mInflater,
                R.layout.service_layout,
                null,
                false
        )

        scaleGestureDetector = ScaleGestureDetector(applicationContext, ServiceGestureDetector())
        gestureDetector = GestureDetector(applicationContext, CustomGestureDetector())

        binding.root.setOnTouchListener(mViewTouchListener)

        binding.play = play
        binding.pause = pause
        binding.exit = exit

        binding.serviceLayoutVideo.holder.addCallback(this)

        mediaPlayer = MediaPlayer()

        mParams = (if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
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
        }).apply {
            gravity = Gravity.BOTTOM or Gravity.END
            width = MIN_WIDTH
            height = MIN_HEIGHT
        }

        displayMetrics = applicationContext.resources.displayMetrics

        mManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        mManager.addView(binding.root, mParams)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        ALog.i("onStartCommand")
        this.intent = intent
        currentTime = intent.getIntExtra("currentTime", -1)
        ALog.i((intent.getSerializableExtra("video") as Item).title)
        (intent.getSerializableExtra("video") as Item).let {
            path = it.path
        }
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
            mediaPlayer.setDisplay(binding.serviceLayoutVideo.holder)
            mediaPlayer.prepare()
            if (currentTime != -1) {
                ALog.i(currentTime)
                mediaPlayer.seekTo(currentTime)
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
        mManager.removeView(binding.root)
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
            binding.servicePlay.visibility = View.VISIBLE
            binding.servicePause.visibility = View.VISIBLE
            binding.serviceExit.visibility = View.VISIBLE
            Handler().postDelayed({
                binding.servicePlay.visibility = View.GONE
                binding.servicePause.visibility = View.GONE
                binding.serviceExit.visibility = View.GONE
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
            binding.serviceLayoutVideo.holder.setFixedSize(mW, mH)
            mParams.width = mW
            mParams.height = mH
            ALog.i(mParams.width)
            ALog.i(mParams.height)
            mManager.updateViewLayout(binding.root, mParams)
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
