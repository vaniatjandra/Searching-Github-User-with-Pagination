package com.mvhousedeveloper.tiketdotcomtest.api

import com.google.gson.annotations.SerializedName
import com.mvhousedeveloper.tiketdotcomtest.model.User

data class GithubResponse(
    @SerializedName("total_count") val total: Int = 0,
    @SerializedName("items") val items: List<User> = emptyList(),
    val nextPage: Int? = null
)
