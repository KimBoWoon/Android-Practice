package com.bowoon.android.android_videoview.video;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.logcat.log.ALog;
import com.bowoon.android.android_videoview.R;
import com.bowoon.android.android_videoview.gif.GIFExtractor;
import com.bowoon.android.android_videoview.gif.GIFView;
import com.bowoon.android.android_videoview.vo.Item;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoPlayerActivity extends Activity implements SurfaceHolder.Callback {
    private final String TAG = "VideoPlayerActivity";
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private MediaPlayer mMediaPlayer;
    private RelativeLayout mVideoInfoView;
    private TextView mVideoTitle, mVideoTime;
    private SeekBar mVideoTimeSeekBar;
    private boolean mSeekBarFlag, isPlay;
    private Item mVideoItem;
    private DisplayMetrics mDisplayMetrics;
    private Button mPlayBtn, mPauseBtn, mDialogBtn, mServiceBtn;
    private long mStartTime, mEndTime;
    private int mFps;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_surfaceview);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent intent = getIntent();
        mVideoItem = (Item) intent.getSerializableExtra("videoContent");

        initView();
        registerListener();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i(TAG, "onConfigurationChanged");

        arrangeVideo();
    }

    private void initView() {
        ButterKnife.bind(this);
        mMediaPlayer = new MediaPlayer();
        mVideoInfoView = (RelativeLayout) findViewById(R.id.video_information);
        mVideoTitle = (TextView) findViewById(R.id.play_video_title);
        mVideoTime = (TextView) findViewById(R.id.video_time);
        mSurfaceView = (SurfaceView) findViewById(R.id.main_surfaceview);
        mVideoTimeSeekBar = (SeekBar) findViewById(R.id.video_seekbar);
        mPlayBtn = (Button) findViewById(R.id.video_play);
        mPauseBtn = (Button) findViewById(R.id.video_pause);
        mDialogBtn = (Button) findViewById(R.id.make_gif);
        mServiceBtn = (Button) findViewById(R.id.video_service);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mDisplayMetrics = getApplicationContext().getResources().getDisplayMetrics();

        mVideoTitle.setText(mVideoItem.getTitle());
    }

    private void registerListener() {
        mVideoTimeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seekBar.getMax() == progress) {
                    mMediaPlayer.stop();
                    finish();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mSeekBarFlag = false;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mSeekBarFlag = true;
                int position = seekBar.getProgress();
                mMediaPlayer.seekTo(position);
                new ProgressSeekBar().start();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            mVideoInfoView.setVisibility(View.VISIBLE);
            mVideoTitle.setVisibility(View.VISIBLE);
//            mDialogBtn.setVisibility(View.VISIBLE);
            mServiceBtn.setVisibility(View.VISIBLE);
            mVideoTime.setText(getStringTime(mMediaPlayer.getCurrentPosition()) + " / " + getStringTime(mMediaPlayer.getDuration()));

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mVideoInfoView.setVisibility(View.GONE);
                    mVideoTitle.setVisibility(View.GONE);
//                    mDialogBtn.setVisibility(View.GONE);
                    mServiceBtn.setVisibility(View.GONE);
                }
            }, 5000);

            return false;
        }
        return super.onTouchEvent(event);
    }

    @OnClick({R.id.make_gif, R.id.video_play, R.id.video_pause, R.id.video_service})
    public void buttonOnClickMethod(View view) {
        switch (view.getId()) {
            case R.id.video_service:
                ALog.i("startService");
                Intent serviceIntent = new Intent(getApplicationContext(), VideoService.class);
                serviceIntent.putExtra("video", mVideoItem);
                serviceIntent.putExtra("currentTime", mMediaPlayer.getCurrentPosition());
                ALog.i(mMediaPlayer.getCurrentPosition());
                startService(serviceIntent);
                finish();
                break;
            case R.id.make_gif:
                mMediaPlayer.pause();
                mSeekBarFlag = false;
                AlertDialog.Builder builder = new AlertDialog.Builder(VideoPlayerActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View v = inflater.inflate(R.layout.dialog_layout, null);
                builder.setView(v);
                Button make = (Button) v.findViewById(R.id.gif_extractor);
                final EditText start = (EditText) v.findViewById(R.id.gif_start);
                final EditText end = (EditText) v.findViewById(R.id.gif_end);
                final EditText fps = (EditText) v.findViewById(R.id.gif_fps);
                final AlertDialog dialog = builder.create();
                make.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mStartTime = Long.valueOf(start.getText().toString());
                        mEndTime = Long.valueOf(end.getText().toString());
                        mFps = Integer.valueOf(fps.getText().toString());

                        GIFExtractor extractor = new GIFExtractor(getApplicationContext());

                        Intent intent = new Intent(VideoPlayerActivity.this, GIFView.class);
                        intent.putExtra("gifImage", extractor.makeGIF(mVideoItem.getPath(), mStartTime, mEndTime, mFps));
                        startActivity(intent);

//                            mMediaPlayer.start();
//                            mSeekBarFlag = true;
//                            new ProgressSeekBar().start();
                        dialog.dismiss();
                    }
                });

                ViewGroup.LayoutParams params = dialog.getWindow().getAttributes();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                dialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

                dialog.show();
                break;
            case R.id.video_play:
                if (!isPlay) {
                    isPlay = true;
                    mMediaPlayer.start();
                }
                break;
            case R.id.video_pause:
                if (isPlay) {
                    isPlay = false;
                    mMediaPlayer.pause();
                }
                break;
            default:
                break;
        }
    }

    // 영상 비율에 맞게 확대 / 축소
    private void arrangeVideo() {
        int videoWidth = mMediaPlayer.getVideoWidth();
        int videoHeight = mMediaPlayer.getVideoHeight();

        int screenWidth = mDisplayMetrics.widthPixels;
        int screenHeight = mDisplayMetrics.heightPixels;

        ViewGroup.LayoutParams lp = mSurfaceView.getLayoutParams();

        if (screenWidth < screenHeight) {
            lp.width = screenWidth;
            lp.height = (int) (((float) videoHeight / (float) videoWidth) * (float) screenWidth);
        } else {
            lp.width = (int) (((float) videoWidth / (float) videoHeight) * (float) screenHeight);
            lp.height = screenHeight;
        }

        mSurfaceView.setLayoutParams(lp);
    }

    private void playVideo(String path) {
        try {
            if (mMediaPlayer == null) {
                mMediaPlayer = new MediaPlayer();
            }
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.setDisplay(mSurfaceHolder);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void releaseMediaPlayer() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mSeekBarFlag = false;
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onResume() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onResume();
        Log.i(TAG, "onResume");
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
        Log.i(TAG, "surfaceCreated");
        mSeekBarFlag = true;
        isPlay = true;
        playVideo(mVideoItem.getPath());
        mVideoTimeSeekBar.setMax(mMediaPlayer.getDuration());
        new ProgressSeekBar().start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i(TAG, "surfaceChanged");
        if (mMediaPlayer != null) {
            mMediaPlayer.setDisplay(holder);
            arrangeVideo();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i(TAG, "surfaceDestroyed");
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
            while (mSeekBarFlag) {
                mVideoTimeSeekBar.setProgress(mMediaPlayer.getCurrentPosition());
            }
        }
    }
}
