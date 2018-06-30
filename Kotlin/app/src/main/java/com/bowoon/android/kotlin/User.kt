package com.bowoon.android.kotlin

data class User(var picture: HashMap<String, String>, var name: HashMap<String, String>) {
    override fun toString(): String {
        return name["title"] + " " + name["first"] + " " + name["last"] + " " + picture["large"] + " " + picture["medium"] + " " + picture["thumbnail"]
    }
}

data class RandomUser(var results: ArrayList<User>)