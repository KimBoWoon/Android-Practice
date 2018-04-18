package com.bowoon.android.android_videoview.video;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
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
    private Button playBtn;
    private Button pauseBtn;
    private Intent intent;
    private DisplayMetrics displayMetrics;
    private boolean isPause;

    @Override
    public void onCreate() {
        super.onCreate();

        ALog.i("onCreate");

        try {
            displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
            int width = displayMetrics.widthPixels;
            int height = displayMetrics.heightPixels;

            LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = mInflater.inflate(R.layout.service_layout, null);

            mView.setOnTouchListener(mViewTouchListener);

            surfaceView = (SurfaceView) mView.findViewById(R.id.service_layout_video);
            playBtn = (Button) mView.findViewById(R.id.service_play);
            pauseBtn = (Button) mView.findViewById(R.id.service_pause);
            exitBtn = (Button) mView.findViewById(R.id.service_exit);
            playBtn.setOnClickListener(listener);
            pauseBtn.setOnClickListener(listener);
            exitBtn.setOnClickListener(listener);

            surfaceHolder = surfaceView.getHolder();
            surfaceHolder.addCallback(this);

            mediaPlayer = new MediaPlayer();

//            mParams = new WindowManager.LayoutParams(
//                    WindowManager.LayoutParams.WRAP_CONTENT,
//                    WindowManager.LayoutParams.WRAP_CONTENT,
//                    WindowManager.LayoutParams.TYPE_PHONE,
//                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                    PixelFormat.TRANSLUCENT);
            mParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                    PixelFormat.TRANSLUCENT);
            mParams.gravity = Gravity.BOTTOM | Gravity.END;

            ALog.i(displayMetrics.widthPixels / 120);
            ALog.i(displayMetrics.heightPixels / 120);

            Point point = screenRatio(displayMetrics);

            if (displayMetrics.widthPixels * 16 > displayMetrics.heightPixels * 9) {
                width = displayMetrics.heightPixels * point.y / point.x;
            } else {
                height = displayMetrics.widthPixels * point.x / point.y;
            }

            ALog.i(width);
            ALog.i(height);

            mParams.width = height;
            mParams.height = width;

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
        startForeground(startId, new Notification());

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
        if (mediaPlayer != null) {
            mediaPlayer.setDisplay(holder);
        }
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

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public void onDestroy() {
        super.onDestroy();
        ALog.i("onDestroy");
        stopSelf();
        stopForeground(true);
        if (mView != null) {
            mManager.removeView(mView);
            mView = null;
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        ALog.i("onTaskRemoved");
        // 여기에 필요한 코드를 추가한다.
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
        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.service_play:
                    if (isPause) {
                        isPause = false;
                        mediaPlayer.start();
                    }
                    break;
                case R.id.service_pause:
                    if (mediaPlayer.isPlaying()) {
                        isPause = true;
                        mediaPlayer.pause();
                    }
                    break;
                case R.id.service_exit:
                    releaseMediaPlayer();
                    stopForeground(true);
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
                    playBtn.setVisibility(View.VISIBLE);
                    pauseBtn.setVisibility(View.VISIBLE);
                    exitBtn.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            playBtn.setVisibility(View.GONE);
                            pauseBtn.setVisibility(View.GONE);
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
//                        double distance = spacing(event);
//                        mParams.width = (int) distance;
//                        if (displayMetrics.widthPixels != mParams.width) {
//                            mParams.height = mParams.height + 5;
//                        }
                        Point point = screenRatio(displayMetrics);
                        int width = 0, height = 0;

                        if (displayMetrics.widthPixels * 16 > displayMetrics.heightPixels * 9) {
                            width = displayMetrics.heightPixels * point.y / point.x;
                        } else {
                            height = displayMetrics.widthPixels * point.x / point.y;
                        }

                        ALog.i(width);
                        ALog.i(height);

//                        mParams.width = height;
//                        mParams.height = width;
//                        mManager.updateViewLayout(mView, mParams);
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

    private Point screenRatio(DisplayMetrics displayMetrics) {
        int max, min, gcd, widthPixels, heightPixels;
        Point result = new Point();

        widthPixels = displayMetrics.widthPixels;
        heightPixels = displayMetrics.heightPixels;

        if (widthPixels < heightPixels) {
            max = widthPixels;
            min = heightPixels;
        } else {
            max = heightPixels;
            min = widthPixels;
        }

        gcd = getGCD(max, min);

        result.x = widthPixels / gcd;
        result.y = heightPixels / gcd;

        return result;
    }

    public int getGCD(int a, int b) {
        while (b != 0) {
            int temp = a % b;
            a = b;
            b = temp;
        }
        return Math.abs(a);
    }
}
