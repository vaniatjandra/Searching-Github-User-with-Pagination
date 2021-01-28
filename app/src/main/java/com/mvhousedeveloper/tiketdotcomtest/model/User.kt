package com.mvhousedeveloper.tiketdotcomtest.model

import com.google.gson.annotations.SerializedName

data class User(
    @field:SerializedName("login") val login: String,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("avatar_url") val avatarUrl: String,
    @field:SerializedName("html_url") val url: String
)
