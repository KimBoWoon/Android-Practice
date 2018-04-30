package com.bowoon.android.android_videoview.gif;

import java.io.ByteArrayOutputStream;

public class GIFExtractor {
    private AnimatedGifEncoder encoder;
    private ByteArrayOutputStream outputStream;

    public GIFExtractor() {
        encoder = new AnimatedGifEncoder();
        outputStream = new ByteArrayOutputStream();
    }

    public void makeGIF() {
        encoder.start(outputStream);
    }
}
