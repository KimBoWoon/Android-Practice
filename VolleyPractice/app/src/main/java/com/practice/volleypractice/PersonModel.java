package com.practice.volleypractice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Null on 2017-09-26.
 */

public class PersonModel {
    @SerializedName("results")
    @Expose
    private List<Person> persons = null;
    @SerializedName("page")
    @Expose
    private int page;

    class Person {
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

        public class Name {
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

        public class UserProfileImage {
            @SerializedName("large")
            @Expose
            private String large;
            @SerializedName("medium")
            @Expose
            private String medium;
            @SerializedName("thumbnail")
            @Expose
            private String thumbnail;

            public String getLarge() {
                return large;
            }

            public void setLarge(String large) {
                this.large = large;
            }

            public String getMedium() {
                return medium;
            }

            public void setMedium(String medium) {
                this.medium = medium;
            }

            public String getThumbnail() {
                return thumbnail;
            }

            public void setThumbnail(String thumbnail) {
                this.thumbnail = thumbnail;
            }
        }

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

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}