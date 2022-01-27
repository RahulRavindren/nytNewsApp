package com.pomeloassignment.android

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pomeloassignment.android.db.ArticleDao
import com.pomeloassignment.android.db.ArticleDatabase
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewsDbSourceTest {
    private lateinit var articleDao: ArticleDao
    private lateinit var articleDB: ArticleDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        articleDB = Room.inMemoryDatabaseBuilder(context, ArticleDatabase::class.java).build()
        articleDao = articleDB.articleDao()
    }


}