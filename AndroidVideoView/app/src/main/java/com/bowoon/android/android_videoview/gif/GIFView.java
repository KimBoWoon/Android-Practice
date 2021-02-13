//package com.bowoon.android.android_videoview.gif;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import androidx.annotation.Nullable;
//import android.util.Log;
//
//import com.bowoon.android.android_videoview.R;
//
//import java.io.IOException;
//import java.util.Arrays;
//
//import pl.droidsonroids.gif.GifDrawable;
//import pl.droidsonroids.gif.GifImageView;
//
//public class GIFView extends Activity {
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.gif_view);
//
//        Intent intent = getIntent();
//        byte[] bytes = intent.getByteArrayExtra("gifImage");
//
//        Log.i("GIFView", Arrays.toString(bytes));
//
//        GifImageView gifImageView = (GifImageView) findViewById(R.id.gif_view);
//
//        try {
//            GifDrawable gifUriDrawable = new GifDrawable(bytes);
//            gifUriDrawable.setLoopCount(0);
//            gifImageView.setImageDrawable(gifUriDrawable);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
