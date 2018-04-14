package com.bowoon.android.android_videoview.gesture;

import android.view.GestureDetector;
import android.view.MotionEvent;

import com.bowoon.android.android_videoview.callback.TouchCallback;

public class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener {
    private TouchCallback touchCallback;

    public CustomGestureDetector(TouchCallback touchCallback) {
        super();
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
        return touchCallback.onDoubleTap(e);
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
