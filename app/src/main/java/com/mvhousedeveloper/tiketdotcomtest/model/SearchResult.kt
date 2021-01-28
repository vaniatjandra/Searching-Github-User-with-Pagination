package com.mvhousedeveloper.tiketdotcomtest.model

import java.lang.Exception

sealed class SearchResult {
    data class Success(val data: List<User>) : SearchResult()
    data class Error(val error: Exception) : SearchResult()
}
