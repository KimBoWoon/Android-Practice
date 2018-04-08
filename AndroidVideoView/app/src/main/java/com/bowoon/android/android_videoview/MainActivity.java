package com.bowoon.android.android_videoview;

import android.content.Intent;
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
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

import com.android.logcat.log.ALog;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private final int GALLERY_CODE = 1112;
    private ImageView imageView;
    private MediaPlayer mediaPlayer;
    private SurfaceView mPreview;
    private SurfaceHolder holder;
    private CustomHolderCallback customHolderCallback;
    private CustomVideoSize customVideoSize;
    private Uri videoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ALog.logSetting(getApplicationContext(), true, false);
        ALog.setDebug(true);

//        imageView = (ImageView) findViewById(R.id.main_image);
        mPreview = (SurfaceView) findViewById(R.id.main_surfaceview);
        customHolderCallback = new CustomHolderCallback();
        customVideoSize = new CustomVideoSize();
        holder = mPreview.getHolder();
        holder.addCallback(customHolderCallback);

        selectGallery();
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

    private void sendPicture(Uri imgUri) {
        String imagePath = getRealPathFromURI(imgUri); // path 경로
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int exifDegree = exifOrientationToDegrees(exifOrientation);

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);//경로를 통해 비트맵으로 전환
        imageView.setImageBitmap(rotate(bitmap, exifDegree));//이미지 뷰에 비트맵 넣기
    }

    private void sendVideo(Uri videoUri) {
        try {
            mediaPlayer = new MediaPlayer();
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
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.stop();
    }

    @Override
    public void onBackPressed() {
        selectGallery();
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
            mediaPlayer.stop();
        }
    }

    private class CustomVideoSize implements MediaPlayer.OnVideoSizeChangedListener {
        @Override
        public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {

        }
    }
}
