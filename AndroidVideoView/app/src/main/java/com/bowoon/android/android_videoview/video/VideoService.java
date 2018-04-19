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
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

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
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;

    @Override
    public void onCreate() {
        super.onCreate();

        initView();
    }

    private void initView() {
        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = mInflater.inflate(R.layout.service_layout, null);

        scaleGestureDetector = new ScaleGestureDetector(getApplicationContext(), new ServiceGestureDetector());
        gestureDetector = new GestureDetector(getApplicationContext(), new CustomGestureDetector());

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

        mParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                PixelFormat.TRANSLUCENT);
        mParams.gravity = Gravity.BOTTOM | Gravity.END;

        displayMetrics = getApplicationContext().getResources().getDisplayMetrics();

        mParams.width = 640;
        mParams.height = 320;

        mManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mManager.addView(mView, mParams);
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
            holder.setFixedSize(width, height);
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
            int count = event.getPointerCount();

            if (count == 1 && event.getAction() == MotionEvent.ACTION_MOVE) {
                int x = (int) (mTouchX - event.getRawX());
                int y = (int) (mTouchY - event.getRawY());

                mParams.x = mViewX + x;
                mParams.y = mViewY + y;

                mManager.updateViewLayout(mView, mParams);

                return true;
            }

            gestureDetector.onTouchEvent(event);
            scaleGestureDetector.onTouchEvent(event);
            return true;
        }
    };

    private class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener {
        public CustomGestureDetector() {
            super();
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
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

            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public void onShowPress(MotionEvent e) {
            super.onShowPress(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            mTouchX = e.getRawX();
            mTouchY = e.getRawY();
            mViewX = mParams.x;
            mViewY = mParams.y;

            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            int screenDivision = getApplicationContext().getResources().getDisplayMetrics().widthPixels / 2;

            if (e.getX() > screenDivision) {
                Toast.makeText(getApplicationContext(), "10초 앞으로", Toast.LENGTH_SHORT).show();
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 10000);
            } else {
                Toast.makeText(getApplicationContext(), "10초 뒤로", Toast.LENGTH_SHORT).show();
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 10000);
            }

            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return super.onDoubleTapEvent(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return super.onSingleTapConfirmed(e);
        }
    }

    private class ServiceGestureDetector extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private int mW, mH;
        private final int MIN_WIDTH = 640;

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            ALog.d("onScale");
            mW *= detector.getScaleFactor();
            mH *= detector.getScaleFactor();
            if (mW <= MIN_WIDTH) {
                mW = 640;
                mH = 360;
            } else if (mW >= displayMetrics.widthPixels) {
                mW = displayMetrics.widthPixels;
                mH = computeRatio(16, 9, displayMetrics).y;
            }
            ALog.d("scale=" + detector.getScaleFactor() + ", w=" + mW + ", h=" + mH);
            surfaceHolder.setFixedSize(mW, mH);
            mParams.width = mW;
            mParams.height = mH;
            ALog.i(mParams.width);
            ALog.i(mParams.height);
            mManager.updateViewLayout(mView, mParams);
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            ALog.d("onScaleBegin");
            mW = mParams.width;
            mH = mParams.height;
            ALog.d("scale=" + detector.getScaleFactor() + ", w=" + mW + ", h=" + mH);
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            ALog.d("onScaleEnd");
            ALog.d("scale=" + detector.getScaleFactor() + ", w=" + mW + ", h=" + mH);
        }

        public Point computeRatio(int widthRatio, int heightRatio, DisplayMetrics displayMetrics) {
            int width = displayMetrics.widthPixels;
            int height = displayMetrics.heightPixels;
            Point point = screenRatio(displayMetrics);
            Point result = new Point();

            if (displayMetrics.widthPixels * widthRatio > displayMetrics.heightPixels * heightRatio) {
                width = displayMetrics.heightPixels * point.y / point.x;
            } else {
                height = displayMetrics.widthPixels * point.x / point.y;
            }

            result.x = width;
            result.y = height;

            ALog.i(result.x);
            ALog.i(result.y);

            return result;
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
}
