package com.bowoon.android.android_http_spi.common;

public class FileDataPart {
    private String fileName;
    private byte[] content;
    private String type;

    public FileDataPart() {
    }

    public FileDataPart(String name, byte[] data) {
        fileName = name;
        content = data;
    }

    public FileDataPart(String name, byte[] data, String mimeType) {
        fileName = name;
        content = data;
        type = mimeType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}