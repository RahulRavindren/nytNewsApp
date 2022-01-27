package com.pomeloassignment.android.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "article")
data class ArticleEntity(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id") var id: Long? = 0L,
    @ColumnInfo(name = "url") var url: String? = "",
    @ColumnInfo(name = "uri") var uri: String? = "",
    @ColumnInfo(name = "source") var source: String? = "",
    @ColumnInfo(name = "published_date") var publishedDate: String? = "",
    @ColumnInfo(name = "section") var section: String? = "",
    @ColumnInfo(name = "image_url") var imageUrl: String? = "",
    @ColumnInfo(name = "title") var title: String? = "",
    @ColumnInfo(name = "abstract") var abstract: String? = ""
) : Parcelable