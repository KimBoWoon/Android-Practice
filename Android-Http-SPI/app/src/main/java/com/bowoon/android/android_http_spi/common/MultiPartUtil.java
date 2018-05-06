package com.bowoon.android.android_http_spi.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class MultiPartUtil {
    private final String TWO_HYPHENS = "--";
    private final String LINE_FEED = "\r\n";
    private final String BOUNDARY = "apiclient-" + System.currentTimeMillis();
    private ByteArrayOutputStream bos;
    private DataOutputStream dos;

    public MultiPartUtil() {
        bos = new ByteArrayOutputStream();
        dos = new DataOutputStream(bos);
    }

    public void textParse(Map<String, String> params, String encoding) throws IOException {
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                buildTextPart(entry.getKey(), entry.getValue());
            }
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + encoding, uee);
        }
    }

    public void dataParse(Map<String, FileDataPart> data) throws IOException {
        for (Map.Entry<String, FileDataPart> entry : data.entrySet()) {
            buildDataPart(entry.getValue(), entry.getKey());
        }
    }

    private void buildTextPart(String parameterName, String parameterValue) throws IOException {
        dos.writeBytes(TWO_HYPHENS + BOUNDARY + LINE_FEED);
        dos.writeBytes("Content-Disposition: form-data; name=\"" + parameterName + "\"" + LINE_FEED);
        dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + LINE_FEED);
        dos.writeBytes(LINE_FEED);
        dos.writeUTF(parameterValue + LINE_FEED);
    }

    private void buildDataPart(FileDataPart dataFile, String inputName) throws IOException {
        dos.writeBytes(TWO_HYPHENS + BOUNDARY + LINE_FEED);
        dos.writeBytes("Content-Disposition: form-data; name=\"" +
                inputName + "\"; filename=\"" + dataFile.getFileName() + "\"" + LINE_FEED);
        if (dataFile.getType() != null && !dataFile.getType().trim().isEmpty()) {
            dos.writeBytes("Content-Type: " + dataFile.getType() + LINE_FEED);
        }
        dos.writeBytes("Content-Transfer-Encoding: binary" + LINE_FEED);
        dos.writeBytes(LINE_FEED);

        ByteArrayInputStream fileInputStream = new ByteArrayInputStream(dataFile.getContent());
        int bytesAvailable = fileInputStream.available();

        int maxBufferSize = 1024 * 1024;
        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
        byte[] buffer = new byte[bufferSize];

        int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

        while (bytesRead > 0) {
            dos.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        }

        dos.writeBytes(LINE_FEED);
        dos.flush();
    }

    public void flush() throws IOException {
        dos.writeBytes(TWO_HYPHENS + BOUNDARY + TWO_HYPHENS + LINE_FEED);
    }

    public ByteArrayOutputStream getBos() {
        return bos;
    }
}
