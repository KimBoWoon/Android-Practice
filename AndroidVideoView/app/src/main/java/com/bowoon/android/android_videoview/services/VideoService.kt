package com.bowoon.android.android_videoview.services

import android.annotation.TargetApi
import android.app.Notification
import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.media.MediaPlayer
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.android.logcat.log.ALog
import com.bowoon.android.android_videoview.R
import com.bowoon.android.android_videoview.databinding.ServiceLayoutBinding
import com.bowoon.android.android_videoview.model.Video
import com.bowoon.android.android_videoview.utils.Utils
import java.io.IOException


interface SetOnVideoButtonClickListener {
    fun onClick()
}

class VideoService : Service() {
    private var currentTime: Int = 0
    private var windowManager: WindowManager? = null
    private var params: WindowManager.LayoutParams? = null
    private var touchX: Float = 0f
    private var touchY: Float = 0f
    private var viewX: Int = 0
    private var viewY: Int = 0
    private var mediaPlayer: MediaPlayer? = null
    private var intent: Intent? = null
    private var item: Video? = null
    private var isPause: Boolean = false
    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var gestureDetector: GestureDetector? = null
    private var binding: ServiceLayoutBinding? = null

    companion object {
        const val MIN_WIDTH = 540
        const val MIN_HEIGHT = 304
        const val TAG = "VideoService"
    }

    private val play: SetOnVideoButtonClickListener = object : SetOnVideoButtonClickListener {
        override fun onClick() {
            isPause = false
            mediaPlayer?.start()
        }
    }

    private val pause: SetOnVideoButtonClickListener = object : SetOnVideoButtonClickListener {
        override fun onClick() {
            isPause = true
            mediaPlayer?.pause()
        }
    }

    private val exit: SetOnVideoButtonClickListener = object : SetOnVideoButtonClickListener {
        override fun onClick() {
            releaseMediaPlayer()
            stopForeground(true)
            stopService(intent)
        }
    }

    private val mViewTouchListener = View.OnTouchListener { _, event ->
        val count = event.pointerCount

        if (count == 1 && event.action == MotionEvent.ACTION_MOVE) {
            val x = (touchX - event.rawX).toInt()
            val y = (touchY - event.rawY).toInt()

            params?.x = viewX + x
            params?.y = viewY + y

            windowManager?.updateViewLayout(binding?.root, params)

            return@OnTouchListener true
        }

        gestureDetector?.onTouchEvent(event)
        scaleGestureDetector?.onTouchEvent(event)
        true
    }

    override fun onCreate() {
        super.onCreate()

        initView()
    }

    private fun initView() {
        windowManager = Utils.getWindowManager(this)
        scaleGestureDetector = ScaleGestureDetector(this, ServiceGestureDetector())
        gestureDetector = GestureDetector(this, CustomGestureDetector())
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(this),
                R.layout.service_layout,
                null,
                false)

        binding?.let { binding ->
            binding.root.setOnTouchListener(mViewTouchListener)

            binding.play = play
            binding.pause = pause
            binding.exit = exit

            binding.serviceLayoutVideo.holder.addCallback(SurfaceHolderCallback())

            mediaPlayer = MediaPlayer()

            params = (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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

            windowManager?.addView(binding.root, params)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        ALog.i("onStartCommand")
        currentTime = intent.getIntExtra("currentTime", -1)
        (intent.getParcelableExtra("video") as Video).let {
            item = it
            this.intent = intent
        }
        startForeground(startId, Notification())

        return super.onStartCommand(intent, flags, startId)
    }

    private fun playVideo(video: Video?) {
        try {
            if (video == null) {
                Toast.makeText(this, "it is invalid file", Toast.LENGTH_SHORT).show()
                return
            }
            mediaPlayer?.setDataSource(video.path)
            mediaPlayer?.setDisplay(binding?.serviceLayoutVideo?.holder)
            mediaPlayer?.prepare()
            if (currentTime != -1) {
                ALog.i(currentTime)
                mediaPlayer?.seekTo(currentTime)
            }
            mediaPlayer?.start()
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
        windowManager?.removeView(binding?.root)
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        ALog.i("onTaskRemoved")
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun releaseMediaPlayer() {
        mediaPlayer?.release()
    }

    private inner class CustomGestureDetector : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            binding?.servicePlay?.visibility = View.VISIBLE
            binding?.servicePause?.visibility = View.VISIBLE
            binding?.serviceExit?.visibility = View.VISIBLE
            Handler().postDelayed({
                binding?.servicePlay?.visibility = View.GONE
                binding?.servicePause?.visibility = View.GONE
                binding?.serviceExit?.visibility = View.GONE
            }, 3000)

            return false
        }

        override fun onDown(e: MotionEvent): Boolean {
            touchX = e.rawX
            touchY = e.rawY
            params?.let {
                viewX = it.x
                viewY = it.y
            }

            return false
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            params?.let { layoutParams ->
                val screenDivision = layoutParams.width / 2

                if (e.x > screenDivision) {
                    Toast.makeText(this@VideoService, "10초 앞으로", Toast.LENGTH_SHORT).show()
                    mediaPlayer?.let { it.seekTo(it.currentPosition + 10000) }
                } else {
                    Toast.makeText(this@VideoService, "10초 뒤로", Toast.LENGTH_SHORT).show()
                    mediaPlayer?.let { it.seekTo(it.currentPosition - 10000) }
                }
            }
            return false
        }
    }

    private inner class ServiceGestureDetector : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        private var mW: Int = 0
        private var mH: Int = 0
        private val displayMetrics = Utils.getDisplayMetrics(this@VideoService)

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            ALog.d("onScale")
            if (mW < MIN_WIDTH) {
                mW = MIN_WIDTH
                return true
            }
            displayMetrics?.let {
                if (mW > it.widthPixels || mH > it.heightPixels) {
                    return true
                }
            }
            mW = (mW.toDouble() * detector.scaleFactor.toDouble()).toInt()
            mH = (mW.toDouble() * (9f / 16f)).toInt()
            ALog.d("mW = $mW, mH = $mH")
            binding?.serviceLayoutVideo?.holder?.setFixedSize(mW, mH)
            params?.width = mW
            params?.height = mH
            windowManager?.updateViewLayout(binding?.root, params)
            return true
        }

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            ALog.d("onScaleBegin")
            params?.let {
                mW = it.width
                mH = it.height
            }
            ALog.d("scale=" + detector.scaleFactor + ", w=" + mW + ", h=" + mH)
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector) {
            ALog.d("onScaleEnd")
            ALog.d("scale=" + detector.scaleFactor + ", w=" + mW + ", h=" + mH)
        }
    }

    private inner class SurfaceHolderCallback : SurfaceHolder.Callback {
        override fun surfaceCreated(holder: SurfaceHolder) {
            ALog.i("surfaceCreated")
            item?.let { playVideo(it) }
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            ALog.i("surfaceChanged")
            holder.setFixedSize(width, height)
            mediaPlayer?.setDisplay(holder)
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            ALog.i("surfaceDestroyed")
        }
    }
}