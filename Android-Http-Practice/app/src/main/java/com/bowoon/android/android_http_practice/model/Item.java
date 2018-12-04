package com.bowoon.android.android_http_practice.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {
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
