package com.pomeloassignment.android.feature.home.repository

import com.pomeloassignment.android.NoResultsException
import com.pomeloassignment.android.db.ArticleEntity
import com.pomeloassignment.android.feature.home.data.INewsDataSource
import com.pomeloassignment.android.feature.home.data.NewsNetworkSource
import com.pomeloassignment.android.model.Article
import com.pomeloassignment.android.model.Result
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepository @Inject constructor(private val newsNetworkSource: INewsDataSource) :
    ISearchRepository {

    override suspend fun searchArticle(query: String): Flow<List<ArticleEntity>> {
        return flow {
            when (val result = newsNetworkSource.fetchSearchResults(query)) {
                is Result.Success -> {
                    if (result.value.isEmpty()) throw NoResultsException()

                    emit(result.value.map {
                        ArticleEntity(
                            id = it.Id?.hashCode()?.toLong() ?: -1,
                            url = it.webUrl,
                            uri = it.uri,
                            imageUrl = "https://static01.nyt.com/${it.multimedia.firstOrNull()?.url}", // image path in search results are relative. Hence the hack
                            title = it.headline?.name ?: "",
                            abstract = it.abstract
                        )
                    })
                }
                is Result.ApiError -> throw Exception("api error")
                else -> Exception("unknown exception")
            }
        }
    }
}

interface ISearchRepository {
    suspend fun searchArticle(query: String): Flow<List<ArticleEntity>>
}