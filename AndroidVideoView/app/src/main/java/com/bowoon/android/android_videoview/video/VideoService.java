package com.bowoon.android.android_videoview.video;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.android.logcat.log.ALog;
import com.bowoon.android.android_videoview.R;
import com.bowoon.android.android_videoview.vo.Item;

import java.io.IOException;

public class VideoService extends Service implements SurfaceHolder.Callback {
    private int currentTime;
    private View mView;
    private WindowManager mManager;
    private WindowManager.LayoutParams mParams;
    private float mTouchX, mTouchY;
    private int mViewX, mViewY;
    private MediaPlayer mediaPlayer;
    private SurfaceHolder surfaceHolder;
    private SurfaceView surfaceView;
    private String path;
    private Button exitBtn;
    private Intent intent;
    private DisplayMetrics displayMetrics;
    private int startId;

    @Override
    public void onCreate() {
        super.onCreate();

        ALog.i("onCreate");

        try {
            displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
            int width = displayMetrics.widthPixels;
            int height = displayMetrics.heightPixels;

            ALog.i(width);
            ALog.i(height);

            LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = mInflater.inflate(R.layout.service_layout, null);

            mView.setOnTouchListener(mViewTouchListener);

            surfaceView = (SurfaceView) mView.findViewById(R.id.service_layout_video);
            exitBtn = (Button) mView.findViewById(R.id.service_exit);
            exitBtn.setOnClickListener(listener);

            surfaceHolder = surfaceView.getHolder();
            surfaceHolder.addCallback(this);

            mediaPlayer = new MediaPlayer();

            mParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
            mParams.gravity = Gravity.BOTTOM | Gravity.END;

            mParams.width = 550;
            mParams.height = 300;

            mManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            mManager.addView(mView, mParams);
        } catch (NullPointerException e) {
            ALog.i("onCreate NullPointer Exception");
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ALog.i("onStartCommand");
        this.intent = intent;
        currentTime = intent.getIntExtra("currentTime", -1);
        Item item = (Item) intent.getSerializableExtra("video");
        path = item.getPath();
        this.startId = startId;

//        startForeground(startId, new Notification());

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        ALog.i("surfaceCreated");
        playVideo(path);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        ALog.i("surfaceChanged");
//        if (mediaPlayer != null) {
//            mediaPlayer.setDisplay(holder);
//            arrangeVideo();
//        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        ALog.i("surfaceDestroyed");
    }

    private void playVideo(String path) {
        try {
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            }
            mediaPlayer.setDataSource(path);
            mediaPlayer.setDisplay(surfaceHolder);
            mediaPlayer.prepare();
            if (currentTime != -1) {
                ALog.i(currentTime);
                mediaPlayer.seekTo(currentTime);
            }
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDestroy() {
        super.onDestroy();
//        stopForeground(startId);
        if (mView != null) {
            mManager.removeView(mView);
            mView = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.service_exit:
                    releaseMediaPlayer();
                    stopService(intent);
                    break;
            }
        }
    };

    private View.OnTouchListener mViewTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int touchCount = event.getPointerCount();

            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    mTouchX = event.getRawX();
                    mTouchY = event.getRawY();
                    mViewX = mParams.x;
                    mViewY = mParams.y;

                    break;
                case MotionEvent.ACTION_UP:
                    exitBtn.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            exitBtn.setVisibility(View.GONE);
                        }
                    }, 3000);
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (touchCount == 1) {
                        int x = (int) (mTouchX - event.getRawX());
                        int y = (int) (mTouchY - event.getRawY());

                        mParams.x = mViewX + x;
                        mParams.y = mViewY + y;

                        mManager.updateViewLayout(mView, mParams);
                    } else if (touchCount == 2) {
                        double distance = spacing(event);
                        mParams.width = (int) distance;
                        if (displayMetrics.widthPixels != mParams.width) {
                            mParams.height = mParams.height + 5;
                        }
                        mManager.updateViewLayout(mView, mParams);
                    }
                    break;
            }

            return true;
        }
    };

    private double spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        double distance = Math.sqrt(x * x + y * y);

        if (distance >= displayMetrics.widthPixels) {
            distance = displayMetrics.widthPixels;
            return distance;
        }

        return distance;
    }
}
