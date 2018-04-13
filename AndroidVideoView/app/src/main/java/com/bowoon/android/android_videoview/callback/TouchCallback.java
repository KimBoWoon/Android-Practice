package com.bowoon.android.android_videoview.callback;

import android.view.MotionEvent;

public interface TouchCallback {
    boolean onDown(MotionEvent event);
    boolean onUp(MotionEvent event);
    boolean onMove(MotionEvent event);
}
