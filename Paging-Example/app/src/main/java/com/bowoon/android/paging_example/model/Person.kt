package com.bowoon.android.paging_example.model

import kotlinx.serialization.Serializable

@Serializable
data class Name(
    val title: String?,
    val first: String?,
    val last: String?
)

@Serializable
data class Picture(
    val large: String?,
    val medium: String?,
    val thumbnail: String?
)

@Serializable
data class Item(
    val name: Name?,
    val picture: Picture?,
    val gender: String?,
    val phone: String? = null
)

@Serializable
data class Info(
    val seed: String?,
    val results: Int?,
    val page: Int?,
    val version: String?
)

@Serializable
data class PersonModel(
    val results: MutableList<Item>?,
    val info: Info?
)