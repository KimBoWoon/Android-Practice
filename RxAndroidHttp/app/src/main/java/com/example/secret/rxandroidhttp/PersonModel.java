package com.example.secret.rxandroidhttp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.net.URL;
import java.util.List;

/**
 * Created by Secret on 2017-09-15.
 */

class Name {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("first")
    @Expose
    private String first;
    @SerializedName("last")
    @Expose
    private String last;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }
}

class UserProfileImage {
    @SerializedName("large")
    @Expose
    private URL large;
    @SerializedName("medium")
    @Expose
    private URL medium;
    @SerializedName("thumbnail")
    @Expose
    private URL thumbnail;

    public URL getLarge() {
        return large;
    }

    public void setLarge(URL large) {
        this.large = large;
    }

    public URL getMedium() {
        return medium;
    }

    public void setMedium(URL medium) {
        this.medium = medium;
    }

    public URL getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(URL thumbnail) {
        this.thumbnail = thumbnail;
    }
}

class Item {
    @SerializedName("name")
    @Expose
    private Name name;
    @SerializedName("picture")
    @Expose
    private UserProfileImage userProfileImage;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("phone")
    @Expose
    private String phone;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public UserProfileImage getUserProfileImage() {
        return userProfileImage;
    }

    public void setUserProfileImage(UserProfileImage userProfileImage) {
        this.userProfileImage = userProfileImage;
    }
}

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
