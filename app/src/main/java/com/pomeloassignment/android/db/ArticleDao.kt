package com.pomeloassignment.android.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ArticleDao {

    @Query("SELECT * FROM article order by published_date")
    fun fetchArticles(): List<ArticleEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertArticle(vararg articleEntity: ArticleEntity)
}