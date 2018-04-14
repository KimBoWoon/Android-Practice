package com.bowoon.android.android_videoview.video;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import com.android.logcat.log.ALog;
import com.bowoon.android.android_videoview.R;

import java.io.IOException;

public class VideoService extends Service implements SurfaceHolder.Callback {
    private int currentTime;
    private final IBinder mBinder = new BindServiceBinder();
    private ServiceCallback mCallback;
    private View mView;
    private WindowManager mManager;
    private WindowManager.LayoutParams mParams;

    private float mTouchX, mTouchY;
    private int mViewX, mViewY;

    private boolean isMove = false;

    private MediaPlayer mediaPlayer;
    private SurfaceHolder surfaceHolder;
    private SurfaceView surfaceView;
    private String path;

    @Override
    public void onCreate() {
        super.onCreate();

        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = mInflater.inflate(R.layout.service_layout, null);

        mView.setOnTouchListener(mViewTouchListener);

        surfaceView = (SurfaceView) mView.findViewById(R.id.service_layout_video);

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

        mParams.width = 320;
        mParams.height = 240;

        mManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mManager.addView(mView, mParams);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        ALog.i("surfaceCreated");
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
            mediaPlayer.seekTo(currentTime);
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public interface ServiceCallback {
        int getCurrentTime();

        String getPath();
    }

    public void registerCallback(ServiceCallback cb) {
        mCallback = cb;
    }

    public void getCurrentTime() {
        ALog.d("called by Activity");
        currentTime = mCallback.getCurrentTime();
    }

    public void getPath() {
        path = mCallback.getPath();
        playVideo(path);
    }

    // Declare inner class
    public class BindServiceBinder extends Binder {
        VideoService getService() {
            return VideoService.this;
        }
    }

    private View.OnTouchListener mViewTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int touchCount = event.getPointerCount();
            double distance;

            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    isMove = false;

                    mTouchX = event.getRawX();
                    mTouchY = event.getRawY();
                    mViewX = mParams.x;
                    mViewY = mParams.y;

                    break;
                case MotionEvent.ACTION_UP:
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (touchCount == 1) {
                        isMove = true;

                        int x = (int) (mTouchX - event.getRawX());
                        int y = (int) (mTouchY - event.getRawY());

                        final int num = 5;
                        if ((x > -num && x < num) && (y > -num && y < num)) {
                            isMove = false;
                            break;
                        }

                        mParams.x = mViewX + x;
                        mParams.y = mViewY + y;

                        mManager.updateViewLayout(mView, mParams);
                    } else if (touchCount == 2) {
                        distance = Math.sqrt(Math.pow(mTouchX - event.getRawX(), 2) + Math.pow(mTouchY - event.getRawY(), 2));
                        ALog.i(distance);
                    }

                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    distance = Math.sqrt(Math.pow(mTouchX - event.getRawX(), 2) + Math.pow(mTouchY - event.getRawY(), 2));
                    ALog.i(distance);
                    break;
            }

            return true;
        }
    };
}
