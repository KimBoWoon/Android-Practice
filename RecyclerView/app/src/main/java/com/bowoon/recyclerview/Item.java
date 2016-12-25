package com.bowoon.recyclerview;

/**
 * Created by 보운 on 2016-12-19.
 */

public class Item {
    private int image;
    private String title;

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Item(int image, String title) {
        this.image = image;
        this.title = title;
    }
}
