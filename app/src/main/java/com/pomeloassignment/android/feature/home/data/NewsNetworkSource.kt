package com.pomeloassignment.android.feature.home.data

import com.pomeloassignment.android.feature.home.repository.NewsService
import com.pomeloassignment.android.model.Article
import com.pomeloassignment.android.model.ArticleSearchDoc
import com.pomeloassignment.android.model.Result
import com.pomeloassignment.android.safeApiCall
import javax.inject.Inject

/*
* Network data source that fetches data from remote API
*/
class NewsNetworkSource @Inject constructor(private val newsService: NewsService) :
    INewsDataSource {

    //fetch articles popular by section & viewed criteria
    override suspend fun fetchRecentByPopularity(period: String): Result<List<Article>> {
        return safeApiCall { newsService.fetchByPopular(period).results }
    }

    //search results of articles
    override suspend fun fetchSearchResults(query: String): Result<List<ArticleSearchDoc.ArticleSearch>> {
        return safeApiCall { newsService.searchArticle(query).response.docs }

    }
}

