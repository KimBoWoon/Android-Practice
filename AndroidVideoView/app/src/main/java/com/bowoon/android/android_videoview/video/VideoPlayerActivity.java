package com.bowoon.android.android_videoview.video;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.logcat.log.ALog;
import com.bowoon.android.android_videoview.R;
import com.bowoon.android.android_videoview.callback.TouchCallback;
import com.bowoon.android.android_videoview.gesture.CustomGestureDetector;

import java.io.IOException;

public class VideoPlayerActivity extends Activity implements SurfaceHolder.Callback,
        MediaPlayer.OnVideoSizeChangedListener, TouchCallback {
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private MediaPlayer mediaPlayer;
    private String path;
    private float x, y;
    private RelativeLayout frameLayout;
    private TextView videoTime;
    private GestureDetector gestureDetector;
    private SeekBar seekBar;
    private boolean flag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_surfaceview);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent intent = getIntent();
        path = intent.getStringExtra("path");

        initView();
        registerListener();
    }

    private void initView() {
        mediaPlayer = new MediaPlayer();
        gestureDetector = new GestureDetector(getApplicationContext(), new CustomGestureDetector(getApplicationContext(), mediaPlayer, this));
        frameLayout = (RelativeLayout) findViewById(R.id.video_information);
        videoTime = (TextView) findViewById(R.id.video_time);
        surfaceView = (SurfaceView) findViewById(R.id.main_surfaceview);
        seekBar = (SeekBar) findViewById(R.id.video_seekbar);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
    }

    private void registerListener() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ALog.i("onProgressChanged");
                if (seekBar.getMax() == progress) {
                    mediaPlayer.stop();
                    finish();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                ALog.i("onStartTrackingTouch");
                flag = false;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                ALog.i("onStopTrackingTouch");
                flag = true;
                int position = seekBar.getProgress();
                mediaPlayer.seekTo(position);
                new ProgressSeekBar().start();
            }
        });
    }

    private void arrangeVideo() {
        int videoWidth = mediaPlayer.getVideoWidth();
        int videoHeight = mediaPlayer.getVideoHeight();
        ALog.d("video :" + videoWidth + "/" + videoHeight);

        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;
        ALog.d("screen : " + screenWidth + "/" + screenHeight);

        android.view.ViewGroup.LayoutParams lp = surfaceView.getLayoutParams();

        //portrat
        if (screenWidth < screenHeight) {
            lp.width = screenWidth;
            lp.height = (int) (((float) videoHeight / (float) videoWidth) * (float) screenWidth);
        } else {
            lp.width = (int) (((float) videoWidth / (float) videoHeight) * (float) screenHeight);
            lp.height = screenHeight;
        }
        ALog.d(lp.width + "/" + lp.height);
        surfaceView.setLayoutParams(lp);
    }

    private void playVideo(String path) {
        try {
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            }
            mediaPlayer.setDataSource(path);
            mediaPlayer.setDisplay(surfaceHolder);
            mediaPlayer.prepare();
            mediaPlayer.setOnVideoSizeChangedListener(this);
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void releaseMediaPlayer() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        flag = false;
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onStop() {
        releaseMediaPlayer();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        releaseMediaPlayer();
        super.onDestroy();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        ALog.i("surfaceCreated");
        flag = true;
        playVideo(path);
        seekBar.setMax(mediaPlayer.getDuration());
        new ProgressSeekBar().start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        ALog.i("surfaceChanged");
        if (mediaPlayer != null) {
            mediaPlayer.setDisplay(holder);
            arrangeVideo();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        ALog.i("surfaceDestroyed");
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        ALog.i("onVideoSizeChanged");
        surfaceHolder.setFixedSize(mediaPlayer.getVideoWidth(), mediaPlayer.getVideoHeight());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            return this.onMove(event);
        }
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent event) {
        x = event.getX();
        y = event.getY();

        return false;
    }

    @Override
    public boolean onUp(MotionEvent event) {
        frameLayout.setVisibility(View.VISIBLE);
        videoTime.setText(getStringTime(mediaPlayer.getCurrentPosition()) + " / " + getStringTime(mediaPlayer.getDuration()));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                frameLayout.setVisibility(View.GONE);
            }
        }, 5000);

        return false;
    }

    @Override
    public boolean onMove(MotionEvent event) {
        if (event.getX() > x + 50f) {
            x = event.getX();
            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 1000);
        } else if (event.getX() < x - 50f) {
            x = event.getX();
            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 1000);
        }

        return true;
    }

    private String getStringTime(int time) {
        int currentSecond = time / 1000;
        int second = currentSecond % 60;
        int minute = (currentSecond / 60) % 60;
        int hour = currentSecond / 3600;

        return hour + ":" + minute + ":" + second;
    }

    private class ProgressSeekBar extends Thread {
        @Override
        public void run() {
            while (flag) {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
            }
        }
    }
}
