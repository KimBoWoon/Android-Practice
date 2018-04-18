//package com.bowoon.android.android_videoview.gesture;
//
//import android.view.ScaleGestureDetector;
//
//import com.bowoon.android.android_videoview.callback.ServiceTouchCallback;
//
//public class CustomServiceGestureDetector implements ScaleGestureDetector.OnScaleGestureListener {
//    private ServiceTouchCallback serviceTouchCallback;
//
//    public CustomServiceGestureDetector(ServiceTouchCallback serviceTouchCallback) {
//        super();
//        this.serviceTouchCallback = serviceTouchCallback;
//    }
//
//    @Override
//    public boolean onScale(ScaleGestureDetector detector) {
//        return serviceTouchCallback.onScale(detector);
//    }
//
//    @Override
//    public boolean onScaleBegin(ScaleGestureDetector detector) {
//        return serviceTouchCallback.onScaleBegin(detector);
//    }
//
//    @Override
//    public void onScaleEnd(ScaleGestureDetector detector) {
//        serviceTouchCallback.onScaleEnd(detector);
//    }
//}
