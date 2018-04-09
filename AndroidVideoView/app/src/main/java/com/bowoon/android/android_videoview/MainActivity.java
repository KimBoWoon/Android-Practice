package com.bowoon.android.android_videoview;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.logcat.log.ALog;

import java.io.IOException;

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

//    private void sendPicture(Uri imgUri) {
//        String imagePath = getRealPathFromURI(imgUri); // path 경로
//        ExifInterface exif = null;
//        try {
//            exif = new ExifInterface(imagePath);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//        int exifDegree = exifOrientationToDegrees(exifOrientation);
//
//        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);//경로를 통해 비트맵으로 전환
//        imageView.setImageBitmap(rotate(bitmap, exifDegree));//이미지 뷰에 비트맵 넣기
//    }

    private void sendVideo(Uri videoUri) {
        try {
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            }

            mediaPlayer.setDataSource(getApplicationContext(), videoUri);
            mediaPlayer.setDisplay(holder);
            mediaPlayer.setOnVideoSizeChangedListener(customVideoSize);
            mediaPlayer.prepare();
            // mediaPlayer.prepareAsync();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap src, float degree) {
        // Matrix 객체 생성
        Matrix matrix = new Matrix();
        // 회전 각도 셋팅
        matrix.postRotate(degree);
        // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(),
                src.getHeight(), matrix, true);
    }

    private String getRealPathFromURI(Uri contentUri) {
        int column_index = 0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }

        return cursor.getString(column_index);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        switch (newConfig.orientation) {

            case Configuration.ORIENTATION_LANDSCAPE:
                ALog.d("Loading Start - back_imgw");
                break;

            case Configuration.ORIENTATION_PORTRAIT:
                ALog.d("Loading Start - back_img");
                break;

        }

        arrangeVideo();
    }

    private void arrangeVideo() {
        WindowManager wm = getWindowManager();
        if (wm == null) {
            return;
        }
//        LinearLayout ll = (LinearLayout) findViewById(R.id.playBtnLayout);

        if (wm.getDefaultDisplay().getRotation() == Surface.ROTATION_90 || wm.getDefaultDisplay().getRotation() == Surface.ROTATION_270) {
            ALog.d("landscape");
//            ll.setVisibility(View.GONE);
            hideStatusBar();
        } else {
            ALog.d("portrat");
//            ll.setVisibility(View.VISIBLE);
            showStatusBar();
        }

        int videoWidth = mediaPlayer.getVideoWidth();
        int videoHeight = mediaPlayer.getVideoHeight();
        ALog.d("video :" + videoWidth + "/" + videoHeight);
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        ALog.d("screen : " + screenWidth + "/" + screenHeight);

        android.view.ViewGroup.LayoutParams lp = mPreview.getLayoutParams();

        //portrat
        if (screenWidth < screenHeight) {
            lp.width = screenWidth;
            lp.height = (int) (((float) videoHeight / (float) videoWidth) * (float) screenWidth);
        } else {
            lp.width = (int) (((float) videoWidth / (float) videoHeight) * (float) screenHeight);
            lp.height = screenHeight;
        }
        ALog.d(lp.width + "/" + lp.height);
        mPreview.setLayoutParams(lp);
    }

    private void hideStatusBar() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
    }

    private void showStatusBar() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
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
