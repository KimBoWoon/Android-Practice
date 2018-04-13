package com.bowoon.android.android_videoview.gesture;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import com.bowoon.android.android_videoview.callback.TouchCallback;

public class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener {
    private Context context;
    private MediaPlayer mediaPlayer;
    private TouchCallback touchCallback;

    public CustomGestureDetector(Context context, MediaPlayer mediaPlayer, TouchCallback touchCallback) {
        super();

        this.context = context;
        this.mediaPlayer = mediaPlayer;
        this.touchCallback = touchCallback;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return touchCallback.onUp(e);
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
        return touchCallback.onDown(e);
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        int screenDivision = mediaPlayer.getVideoWidth() / 2;

        if (e.getX() > screenDivision) {
            Toast.makeText(context, "10초 앞으로", Toast.LENGTH_SHORT).show();
            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 10000);
        } else {
            Toast.makeText(context, "10초 뒤로", Toast.LENGTH_SHORT).show();
            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 10000);
        }

        return super.onDoubleTap(e);
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
