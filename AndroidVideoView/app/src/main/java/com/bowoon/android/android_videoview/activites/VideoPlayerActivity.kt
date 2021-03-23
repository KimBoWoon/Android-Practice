package com.bowoon.android.android_videoview.activites

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.media.MediaPlayer
import android.media.PlaybackParams
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.MediaController
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
    private var player = MediaPlayer()
    private var controller: MediaController? = null
    private var hideMenu = false
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
        viewModel.video.observe(this) {
            binding.playVideoTitle.text = it.title
        }
        viewModel.resize.observe(this) {
            binding.mainSurfaceView.let { surfaceView ->
                surfaceView.layoutParams.apply {
                    width = it.first
                    height = it.second
                }
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

        binding.videoService.setOnClickListener {
            Log.i(TAG, "startService")
            startService(Intent(this, VideoService::class.java).apply {
                putExtra("video", viewModel.video.value)
                putExtra("currentTime", player.currentPosition)
            })
            finish()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.i(TAG, "onConfigurationChanged")

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show()
            viewModel.resizeSurfaceView(this, player)
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show()
            viewModel.resizeSurfaceView(this, player)
        }
    }

    private fun playVideo(path: Uri) {
        try {
            player.setOnPreparedListener {
                it.start()
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
            player.setOnCompletionListener {
                releaseMediaPlayer()
                this@VideoPlayerActivity.finish()
            }
            player.setDataSource(this, path)
            player.setDisplay(binding.mainSurfaceView.holder)
            player.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun releaseMediaPlayer() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        controller?.hide()
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

    private inner class CustomGestureDetector : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            if (e.action == MotionEvent.ACTION_UP) {
                binding.playVideoTitle.visibility = View.VISIBLE
                binding.videoService.visibility = View.VISIBLE
                controller?.show(5000)

                if (!hideMenu) {
                    hideMenu = true
                    Single.timer(5000, TimeUnit.MILLISECONDS)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    {
                                        binding.playVideoTitle.visibility = View.GONE
                                        binding.videoService.visibility = View.GONE
                                        controller?.hide()
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
                    controller = MediaController(this@VideoPlayerActivity).apply {
                        setMediaPlayer(Controller())
                        setAnchorView(binding.mainSurfaceView)
                        isEnabled = true
                    }
                }
            }
            viewModel.resizeSurfaceView(this@VideoPlayerActivity, player)
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            Log.i(TAG, "surfaceChanged")
            player.setDisplay(holder)
            viewModel.resizeSurfaceView(this@VideoPlayerActivity, player)
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            Log.i(TAG, "surfaceDestroyed")
        }
    }

    private inner class Controller : MediaController.MediaPlayerControl {
        override fun start() {
            player.start()
        }

        override fun pause() {
            player.pause()
        }

        override fun getDuration(): Int = player.duration

        override fun getCurrentPosition(): Int {
            return player.currentPosition
        }

        override fun seekTo(position: Int) {
            player.seekTo(position)
        }

        override fun isPlaying(): Boolean = player.isPlaying

        override fun getBufferPercentage(): Int = 0

        override fun canPause(): Boolean = true

        override fun canSeekBackward(): Boolean = true

        override fun canSeekForward(): Boolean = true

        override fun getAudioSessionId(): Int = 0
    }
}
