package com.pomeloassignment.android.model

data class ArticleResponse<T>(
    val status: String,
    val copyright: String,
    val results: T
)

data class SearchResponse<T>(
    val status: String,
    val copyright: String,
    val response: T
)