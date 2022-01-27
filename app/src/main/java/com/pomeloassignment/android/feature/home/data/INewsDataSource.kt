package com.pomeloassignment.android.feature.home.data

import com.pomeloassignment.android.model.Article
import com.pomeloassignment.android.model.ArticleSearchDoc
import com.pomeloassignment.android.model.Result

interface INewsDataSource {
    suspend fun fetchRecentByPopularity(period: String): Result<List<Article>>
    suspend fun fetchSearchResults(query: String): Result<List<ArticleSearchDoc.ArticleSearch>>
}

