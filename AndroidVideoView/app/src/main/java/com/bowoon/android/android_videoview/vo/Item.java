package com.bowoon.android.android_videoview.vo;

import java.io.Serializable;

public class Item implements Serializable {
    private String title;
    private String path;

    public Item(String path) {
        this.path = path;
    }

    public Item(String title, String path) {
        this.title = title;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
