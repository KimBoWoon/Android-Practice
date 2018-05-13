package com.bowoon.android.android_http_spi.util;

import com.google.android.gms.common.util.IOUtils;

import java.io.File;
import java.io.IOException;

public class Utility {
    public static byte[] fileToByte(File file) {
        try {
            return IOUtils.toByteArray(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
