package com.bowoon.android.android_videoview.gif;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.bowoon.android.android_videoview.R;
import com.bumptech.glide.Glide;

import java.util.Arrays;

public class GIFView extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gif_view);

        Intent intent = getIntent();
        byte[] bytes = intent.getByteArrayExtra("gifImage");

        Log.i("GIFView", Arrays.toString(bytes));

        ImageView imageView = (ImageView) findViewById(R.id.gif_view);

        Glide.with(this).load(bytes).into(imageView);
    }
}
