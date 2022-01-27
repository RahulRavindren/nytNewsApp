package com.pomeloassignment.android.feature.home.repository

import com.pomeloassignment.android.BuildConfig
import com.pomeloassignment.android.model.Article
import com.pomeloassignment.android.model.ArticleResponse
import com.pomeloassignment.android.model.ArticleSearchDoc
import com.pomeloassignment.android.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NewsService {
    @GET("/svc/mostpopular/v2/viewed/{period}.json")
    suspend fun fetchByPopular(
        @Path("period") period: String,
        @Query("api-key") apiKey: String = BuildConfig.API_KEY
    ): ArticleResponse<List<Article>>

    @GET("/svc/search/v2/articlesearch.json")
    suspend fun searchArticle(
        @Query("q") query: String,
        @Query("api-key") apiKey: String = BuildConfig.API_KEY
    ): SearchResponse<ArticleSearchDoc>
}