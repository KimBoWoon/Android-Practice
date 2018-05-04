package com.bowoon.android.android_http_spi.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class PersonModel {
    @SerializedName("results")
    @Expose
    private List<Item> items = null;
    @SerializedName("page")
    @Expose
    private int page;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}