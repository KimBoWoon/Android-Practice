package com.bowoon.android.android_videoview;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.android.logcat.log.ALog;

import java.io.IOException;

public class VideoPlayerActivity extends Activity implements SurfaceHolder.Callback,
        MediaPlayer.OnVideoSizeChangedListener {
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private MediaPlayer mediaPlayer;
    private String path;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_surfaceview);

        Intent intent = getIntent();
        path = intent.getStringExtra("path");

        mediaPlayer = new MediaPlayer();
        surfaceView = (SurfaceView) findViewById(R.id.main_surfaceview);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
    }

    private void arrangeVideo() {
        int videoWidth = mediaPlayer.getVideoWidth();
        int videoHeight = mediaPlayer.getVideoHeight();
        ALog.d("video :" + videoWidth + "/" + videoHeight);
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
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
        playVideo(path);
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
}
