package com.bowoon.android.kotlin

data class User(var name: HashMap<String, String>) {
    override fun toString(): String {
        return name["title"] + " " + name["first"] + " " + name["last"]
    }
}