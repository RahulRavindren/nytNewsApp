package com.pomeloassignment.android.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.pomeloassignment.android.db.ArticleDao
import com.pomeloassignment.android.db.ArticleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DBModule {

    @Provides
    fun articleDao(context: Context): ArticleDao {
        val db = buildDB(context)
        return db.articleDao()
    }

    private fun buildDB(context: Context): ArticleDatabase {
        return Room.databaseBuilder(context, ArticleDatabase::class.java, "article.db")
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                }

                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                }
            })
            .build()
    }
}