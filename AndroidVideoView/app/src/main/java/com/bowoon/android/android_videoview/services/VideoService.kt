package com.bowoon.android.android_videoview.services

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.bowoon.android.android_videoview.R
import com.bowoon.android.android_videoview.databinding.ServiceLayoutBinding
import com.bowoon.android.android_videoview.model.Video
import com.bowoon.android.android_videoview.utils.Utils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import java.io.IOException
import java.util.concurrent.TimeUnit

class VideoService : Service() {
    private var currentTime = 0
    private var params: WindowManager.LayoutParams? = null
    private var touchX = 0f
    private var touchY = 0f
    private var viewX = 0
    private var viewY = 0
    private var player = MediaPlayer()
    private var intent: Intent? = null
    private var item: Video? = null
    private var hideMenu = false
    private val windowManager by lazy {
        Utils.getWindowManager(this)
    }
    private val scaleGestureDetector by lazy {
        ScaleGestureDetector(this, ServiceGestureDetector())
    }
    private val gestureDetector by lazy {
        GestureDetector(this, CustomGestureDetector())
    }
    private val binding by lazy {
        DataBindingUtil.inflate<ServiceLayoutBinding>(LayoutInflater.from(this), R.layout.service_layout, null, false)
    }
    private val isPause = MutableLiveData<Boolean>(false)
    private val pauseObserver: Observer<Boolean> = Observer {
        if (it) {
            player.pause()
        } else {
            player.start()
        }
    }

    companion object {
        const val MIN_WIDTH = 540
        const val MIN_HEIGHT = 304
        const val TAG = "VideoService"
    }

    override fun onCreate() {
        super.onCreate()

        initView()

        isPause.removeObserver(pauseObserver)
        isPause.observeForever(pauseObserver)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {
        binding.root.setOnTouchListener { _, event ->
            val count = event.pointerCount

            if (count == 1 && event.action == MotionEvent.ACTION_MOVE) {
                val x = (touchX - event.rawX).toInt()
                val y = (touchY - event.rawY).toInt()

                params?.x = viewX + x
                params?.y = viewY + y

                windowManager.updateViewLayout(binding.root, params)

                return@setOnTouchListener true
            }

            gestureDetector.onTouchEvent(event)
            scaleGestureDetector.onTouchEvent(event)
            true
        }

        binding.servicePlay.setOnClickListener {
            isPause.value = false
        }
        binding.servicePause.setOnClickListener {
            isPause.value = true
        }
        binding.serviceExit.setOnClickListener {
            releaseMediaPlayer()
            stopForeground(true)
            stopService(intent)
        }

        binding.surfaceView.holder.addCallback(SurfaceHolderCallback())

        player = MediaPlayer()

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

        windowManager.addView(binding.root, params)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.i("", "onStartCommand")
        currentTime = intent.getIntExtra("currentTime", -1)
        (intent.getParcelableExtra("video") as Video).let {
            item = it
            this.intent = intent
        }
        startForeground(startId, NotificationCompat
                .Builder(this@VideoService, startId.toString())
                .setContentTitle(item?.title)
                .setContentText(if (isPause.value!!) "일시정지" else "재생중")
                .build())

        return super.onStartCommand(intent, flags, startId)
    }

    private fun playVideo(video: Video?) {
        try {
            if (video == null) {
                Toast.makeText(this, "it is invalid file", Toast.LENGTH_SHORT).show()
                return
            }
            video.uri?.let { uri ->
                player.setOnPreparedListener {
                    resizeSurfaceView(binding.surfaceView.width, binding.surfaceView.height)
                    player.start()
                }
                player.setDataSource(this, uri)
                player.setDisplay(binding.surfaceView.holder)
                player.prepare()
                if (currentTime != -1) {
                    Log.i("", "$currentTime")
                    player.seekTo(currentTime)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    override fun onDestroy() {
        super.onDestroy()
        Log.i("", "onDestroy")
        stopSelf()
        stopForeground(true)
        windowManager.removeView(binding.root)
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        Log.i("", "onTaskRemoved")
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun releaseMediaPlayer() {
        player.release()
    }

    private inner class CustomGestureDetector : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            binding.servicePlay.visibility = View.VISIBLE
            binding.servicePause.visibility = View.VISIBLE
            binding.serviceExit.visibility = View.VISIBLE
            if (!hideMenu) {
                hideMenu = true

                Single.timer(5000, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                {
                                    binding.servicePlay.visibility = View.GONE
                                    binding.servicePause.visibility = View.GONE
                                    binding.serviceExit.visibility = View.GONE
                                    hideMenu = false
                                },
                                { it.printStackTrace() }
                        )
            }

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
                    player.seekTo(player.currentPosition + 10000)
                } else {
                    Toast.makeText(this@VideoService, "10초 뒤로", Toast.LENGTH_SHORT).show()
                    player.seekTo(player.currentPosition - 10000)
                }
            }
            return false
        }
    }

    private fun resizeSurfaceView(serviceWidth: Int, serviceHeight: Int) {
        var newWidth = player.videoWidth
        var newHeight = player.videoHeight
        var rate = 0.0f
        val max = if (newWidth > newHeight) {
            serviceWidth
        } else {
            serviceHeight
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

        if (newHeight > serviceHeight) {
            newHeight = serviceHeight
        }

        if (newWidth > serviceWidth) {
            newWidth = serviceWidth
        }

        binding.surfaceView.let { surfaceView ->
            surfaceView.layoutParams.apply {
                width = newWidth
                height = newHeight
            }
        }
    }

    private inner class ServiceGestureDetector : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        private var mW: Int = 0
        private var mH: Int = 0
        private val displayMetrics = Utils.getDisplayMetrics(this@VideoService)

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            Log.d("", "onScale")
            if (mW < MIN_WIDTH) {
                mW = MIN_WIDTH
                return true
            }
            displayMetrics.let {
                if (mW > it.widthPixels) {
                    mW = it.widthPixels
                    return true
                }
                if (mH > it.heightPixels) {
                    mH = it.heightPixels
                    return true
                }
            }
            mW = (mW.toDouble() * detector.scaleFactor.toDouble()).toInt()
            mH = (mW.toDouble() * (9f / 16f)).toInt()
            Log.d("", "mW = $mW, mH = $mH")
            binding.surfaceView.holder?.setFixedSize(mW, mH)
            params?.width = mW
            params?.height = mH
            resizeSurfaceView(mW, mH)
            windowManager.updateViewLayout(binding.root, params)
            return true
        }

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            Log.d("","onScaleBegin")
            params?.let {
                mW = it.width
                mH = it.height
            }
            Log.d("", "scale=" + detector.scaleFactor + ", w=" + mW + ", h=" + mH)
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector) {
            Log.d("", "onScaleEnd")
            Log.d("", "scale=" + detector.scaleFactor + ", w=" + mW + ", h=" + mH)
        }
    }

    private inner class SurfaceHolderCallback : SurfaceHolder.Callback {
        override fun surfaceCreated(holder: SurfaceHolder) {
            Log.i("", "surfaceCreated")
            item?.let { playVideo(it) }
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            Log.i("", "surfaceChanged")
            holder.setFixedSize(width, height)
            player.setDisplay(holder)
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            Log.i("", "surfaceDestroyed")
        }
    }
}