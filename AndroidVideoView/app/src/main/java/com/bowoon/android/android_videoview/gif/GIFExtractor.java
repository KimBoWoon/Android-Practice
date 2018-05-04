package com.bowoon.android.android_videoview.gif;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class GIFExtractor {
    private AnimatedGifEncoder encoder;
    private ByteArrayOutputStream outputStream;
    private Context context;

    public GIFExtractor() {
        encoder = new AnimatedGifEncoder();
        outputStream = new ByteArrayOutputStream();
    }

    public GIFExtractor(Context context) {
        encoder = new AnimatedGifEncoder();
        outputStream = new ByteArrayOutputStream();
        this.context = context;
    }

    public byte[] makeGIF(String source, long startTime, long endTime, int fps) {
        encoder.start(outputStream);
        encoder.setDelay(getDelayOfFrame(fps));
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        BitmapFactory.Options bitmapOption = new BitmapFactory.Options();

        retriever.setDataSource(source);

        int frame = fps * getSecFromMs(endTime);
        int rate = (int) (endTime / frame);

        Log.i("rate", "rate : " + rate);

        bitmapOption.inJustDecodeBounds = true;
        retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST).compress(Bitmap.CompressFormat.WEBP, 100, stream);
        BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.toByteArray().length, bitmapOption);
        bitmapOption.inSampleSize = calculateInSampleSize(bitmapOption, 320, 180);
//        bitmapOption.inSampleSize = 16;
        bitmapOption.inJustDecodeBounds = false;
        try {
            stream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stream.reset();

        ArrayList<Bitmap> list = new ArrayList<Bitmap>();

        for (long i = startTime; i <= endTime; i += rate) {
            Log.i("makeGIF", String.valueOf(i));
            retriever.getFrameAtTime(i * 1000, MediaMetadataRetriever.OPTION_CLOSEST).compress(Bitmap.CompressFormat.JPEG, 100, stream);
            Bitmap bitmap = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.toByteArray().length, bitmapOption);
            list.add(bitmap);
            try {
                stream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            stream.reset();
        }

        Log.i("listsize", String.valueOf(list.size()));

        for (int i = 0; i < list.size(); i++) {
            Log.i("addFrame", String.valueOf(i));
            saveBitmaptoJpeg(list.get(i), "gif", "image_" + i + ".jpg");
            encoder.addFrame(list.get(i));
        }

        retriever.release();
        encoder.finish();
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        saveBitmaptoJpeg(BitmapFactory.decodeByteArray(outputStream.toByteArray(), 0, outputStream.size(), bitmapOption), "gif", "output.gif");

        return outputStream.toByteArray();
    }

    public void saveBitmaptoJpeg(Bitmap bitmap, String folder, String name) {
        String ex_storage = Environment.getExternalStorageDirectory().getAbsolutePath();
        // Get Absolute Path in External Sdcard
        String foler_name = "/" + folder + "/";
        String file_name = name;
        String string_path = ex_storage + foler_name;

        File file_path;
        try {
            file_path = new File(string_path);
            if (!file_path.isDirectory()) {
                file_path.mkdirs();
            }
            FileOutputStream out = new FileOutputStream(string_path + file_name);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file_path)));
        } catch (FileNotFoundException exception) {
            Log.e("FileNotFoundException", exception.getMessage());
        } catch (IOException exception) {
            Log.e("IOException", exception.getMessage());
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        Log.i("width", String.valueOf(width));
        Log.i("height", String.valueOf(height));

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        Log.i("inSampleSizeMethod", String.valueOf(inSampleSize));

        return inSampleSize;
    }

    public int getSecFromMs(long milliseconds) {
        return (int) (milliseconds / 1000) % 60;
    }

    public int getDelayOfFrame(int fps) {
        return (int) (((double) 1 / fps) * 1000);
    }
}