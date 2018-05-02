package com.bowoon.android.android_videoview.gif;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class GIFExtractor {
    private AnimatedGifEncoder encoder;
    private ByteArrayOutputStream outputStream;
    private int videoTotalTime;

    public GIFExtractor(int videoTotalTime) {
        encoder = new AnimatedGifEncoder();
        outputStream = new ByteArrayOutputStream();
        this.videoTotalTime = videoTotalTime;
    }

    public byte[] makeGIF(String source, long startTime, long endTime) {
        encoder.start(outputStream);
        encoder.setDelay(getDelayOfFrame(10));
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        BitmapFactory.Options bitmapOption = new BitmapFactory.Options();

        retriever.setDataSource(source);

        int frame = 10 * getSecFromMs(endTime);
        int rate = (int) (endTime / frame);

        Log.i("rate", "rate : " + rate);

        bitmapOption.inJustDecodeBounds = true;
        retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST).compress(Bitmap.CompressFormat.WEBP, 100, stream);
        BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.toByteArray().length, bitmapOption);
        bitmapOption.inSampleSize = calculateInSampleSize(bitmapOption, 320, 180);
//        bitmapOption.inSampleSize = 16;
        bitmapOption.inJustDecodeBounds = false;
        stream.reset();

        ArrayList<Bitmap> list = new ArrayList<Bitmap>();

        for (long i = startTime; i <= endTime; i += rate) {
            Log.i("makeGIF", String.valueOf(i));
            retriever.getFrameAtTime(i * 1000).compress(Bitmap.CompressFormat.WEBP, 100, stream);
            Bitmap bitmap = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.toByteArray().length, bitmapOption);
            list.add(bitmap);
            stream.reset();
        }

        Log.i("listsize", String.valueOf(list.size()));

        for (int i = 0; i < list.size(); i++) {
            Log.i("addFrame", String.valueOf(i));
            encoder.addFrame(list.get(i));
        }
        retriever.release();
        encoder.finish();
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return outputStream.toByteArray();
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