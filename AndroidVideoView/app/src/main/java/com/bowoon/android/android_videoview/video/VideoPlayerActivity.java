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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.logcat.log.ALog;
import com.bowoon.android.android_videoview.R;
import com.bowoon.android.android_videoview.callback.TouchCallback;
import com.bowoon.android.android_videoview.gesture.CustomGestureDetector;
import com.bowoon.android.android_videoview.vo.Item;

import java.io.IOException;

public class VideoPlayerActivity extends Activity implements SurfaceHolder.Callback,
        TouchCallback {
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private MediaPlayer mediaPlayer;
    private float x, y;
    private RelativeLayout relativeLayout;
    private TextView videoTitle;
    private TextView videoTime;
    private GestureDetector gestureDetector;
    private SeekBar seekBar;
    private boolean seekBarFlag;
    private int savedTime;
    private Item item;
    private DisplayMetrics displayMetrics;
    private Button serviceStartBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_surfaceview);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent intent = getIntent();
        item = (Item) intent.getSerializableExtra("videoContent");

        initView();
        registerListener();
    }

    private void initView() {
        mediaPlayer = new MediaPlayer();
        gestureDetector = new GestureDetector(getApplicationContext(), new CustomGestureDetector(this));
        relativeLayout = (RelativeLayout) findViewById(R.id.video_information);
        videoTitle = (TextView) findViewById(R.id.play_video_title);
        videoTime = (TextView) findViewById(R.id.video_time);
        surfaceView = (SurfaceView) findViewById(R.id.main_surfaceview);
        seekBar = (SeekBar) findViewById(R.id.video_seekbar);
        serviceStartBtn = (Button) findViewById(R.id.video_service);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
    }

    private void registerListener() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seekBar.getMax() == progress) {
                    mediaPlayer.stop();
                    finish();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBarFlag = false;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBarFlag = true;
                int position = seekBar.getProgress();
                mediaPlayer.seekTo(position);
                new ProgressSeekBar().start();
            }
        });

        serviceStartBtn.setOnClickListener(listener);
    }

    Button.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.video_service:
                    ALog.i("startService");
                    Intent serviceIntent = new Intent(getApplicationContext(), VideoService.class);
                    serviceIntent.putExtra("video", item);
                    serviceIntent.putExtra("currentTime", mediaPlayer.getCurrentPosition());
                    startService(serviceIntent);
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    private void arrangeVideo() {
        int videoWidth = mediaPlayer.getVideoWidth();
        int videoHeight = mediaPlayer.getVideoHeight();

        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        ViewGroup.LayoutParams lp = surfaceView.getLayoutParams();

        if (screenWidth < screenHeight) {
            lp.width = screenWidth;
            lp.height = (int) (((float) videoHeight / (float) videoWidth) * (float) screenWidth);
        } else {
            lp.width = (int) (((float) videoWidth / (float) videoHeight) * (float) screenHeight);
            lp.height = screenHeight;
        }

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
            ALog.i(savedTime);
            mediaPlayer.seekTo(savedTime);
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void releaseMediaPlayer() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        seekBarFlag = false;
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ALog.i(mediaPlayer.getCurrentPosition());
        outState.putInt("mediaPlayerCurrentPosition", mediaPlayer.getCurrentPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        savedTime = savedInstanceState.getInt("mediaPlayerCurrentPosition");
        ALog.i(savedTime);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ALog.i("onPause");
    }

    @Override
    protected void onResume() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onResume();
        ALog.i("onResume");
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
        seekBarFlag = true;
        playVideo(item.getPath());
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
        relativeLayout.setVisibility(View.VISIBLE);
        videoTitle.setText(item.getTitle());
        videoTime.setText(getStringTime(mediaPlayer.getCurrentPosition()) + " / " + getStringTime(mediaPlayer.getDuration()));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                relativeLayout.setVisibility(View.GONE);
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

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        int screenDivision = getApplicationContext().getResources().getDisplayMetrics().widthPixels / 2;

        if (event.getX() > screenDivision) {
            Toast.makeText(getApplicationContext(), "10초 앞으로", Toast.LENGTH_SHORT).show();
            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 10000);
        } else {
            Toast.makeText(getApplicationContext(), "10초 뒤로", Toast.LENGTH_SHORT).show();
            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 10000);
        }

        return false;
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
            while (seekBarFlag) {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
            }
        }
    }
}