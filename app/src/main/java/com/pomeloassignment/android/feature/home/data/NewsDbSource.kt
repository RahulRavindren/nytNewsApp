package com.pomeloassignment.android.feature.home.data

import com.pomeloassignment.android.db.ArticleDao
import com.pomeloassignment.android.db.ArticleEntity
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NewsDbSource @Inject constructor(private val articleDao: ArticleDao) : INewsLocalSource {

    //fetches articles stored in persistence
    override suspend fun insertArticles(vararg articles: ArticleEntity) =
        withContext(Dispatchers.IO) {
            articleDao.insertArticle(*articles)
        }

    override suspend fun fetchArticles(): List<ArticleEntity> =
        withContext(Dispatchers.IO) { articleDao.fetchArticles() }
}

interface INewsLocalSource {
    suspend fun insertArticles(vararg articles: ArticleEntity)
    suspend fun fetchArticles(): List<ArticleEntity>
}