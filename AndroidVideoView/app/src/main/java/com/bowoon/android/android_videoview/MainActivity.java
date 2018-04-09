package com.bowoon.android.android_videoview;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.android.logcat.log.ALog;

public class MainActivity extends AppCompatActivity {
    private final int GALLERY_CODE = 1112;
    private MediaPlayer mediaPlayer;
    private SurfaceView mPreview;
    private SurfaceHolder holder;
    private CustomHolderCallback customHolderCallback;
    private CustomVideoSize customVideoSize;
    private Uri videoUri;
    private long currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ALog.logSetting(getApplicationContext(), true, false);
        ALog.setDebug(true);

        mediaPlayer = new MediaPlayer();
        mPreview = (SurfaceView) findViewById(R.id.main_surfaceview);
        customHolderCallback = new CustomHolderCallback();
        customVideoSize = new CustomVideoSize();
        holder = mPreview.getHolder();
        holder.addCallback(customHolderCallback);
    }

    private void selectGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        startActivityForResult(intent, GALLERY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GALLERY_CODE:
//                    sendPicture(data.getData()); //갤러리에서 가져오기
//                    sendVideo(data.getData());
                    videoUri = data.getData();
                    ALog.i(videoUri);
                    break;
                default:
                    break;
            }

        }
    }

    private void sendVideo(Uri videoUri) {
        try {
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            }

            mediaPlayer.setDataSource(getApplicationContext(), videoUri);
            mediaPlayer.setDisplay(holder);
            mediaPlayer.setOnVideoSizeChangedListener(customVideoSize);
            mediaPlayer.prepare();
//             mediaPlayer.prepareAsync();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        releaseMediaPlayer();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        releaseMediaPlayer();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (currentTime + 2000 > System.currentTimeMillis()) {
            Toast.makeText(getApplicationContext(), "종료", Toast.LENGTH_LONG).show();
            this.finish();
        }
        if (currentTime + 2000 < System.currentTimeMillis()) {
            currentTime = System.currentTimeMillis();
            selectGallery();
        }
    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private class CustomHolderCallback implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            ALog.i("surfaceCreated");
            sendVideo(videoUri);
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            ALog.i("surfaceChanged");
//            if (mediaPlayer != null) {
//                surfaceHolder.setFixedSize(i1, i2);
//                mediaPlayer.setDisplay(surfaceHolder);
//                arrangeVideo();
//            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            ALog.i("surfaceDestroyed");
        }
    }

    private class CustomVideoSize implements MediaPlayer.OnVideoSizeChangedListener {
        @Override
        public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {

        }
    }
}
