package com.pomeloassignment.android.feature.home.repository

import com.pomeloassignment.android.NoResultsException
import com.pomeloassignment.android.db.ArticleEntity
import com.pomeloassignment.android.feature.home.data.INewsDataSource
import com.pomeloassignment.android.feature.home.data.INewsLocalSource
import com.pomeloassignment.android.model.Result
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NewsRepository @Inject constructor(
    private val dbSource: INewsLocalSource,
    private val networkSource: INewsDataSource
) : INewsRepository {

    override suspend fun fetchNews(): Flow<List<ArticleEntity>> {
        return flow {
            val articles = dbSource.fetchArticles()
            if (!articles.isNullOrEmpty()) emit(articles)
            val remoteData = networkSource.fetchRecentByPopularity("7")
            if (remoteData is Result.Success) {
                dbSource.insertArticles(*remoteData.value.map {
                    //NOTE @rahul  - Enhancement to added mappers. Clean code
                    ArticleEntity(
                        it.id,
                        it.url,
                        it.uri,
                        it.source,
                        it.publishedDate,
                        it.section,
                        it.media.lastOrNull()?.mediaMetadata?.firstOrNull()?.url,
                        it.title,
                        it.abstract
                    )
                }.toTypedArray())
                emit(dbSource.fetchArticles())
            } else if (remoteData is Result.ApiError) {
                throw Exception("api error")
            }
        }
    }

    override suspend fun searchNews(q: String): Flow<List<ArticleEntity>> {
        return flow {
            when (val result = networkSource.fetchSearchResults(q)) {
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

interface INewsRepository {
    suspend fun fetchNews(): Flow<List<ArticleEntity>>
    suspend fun searchNews(q: String): Flow<List<ArticleEntity>>
}