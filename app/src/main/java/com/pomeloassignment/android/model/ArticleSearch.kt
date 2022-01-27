package com.pomeloassignment.android.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class ArticleSearchDoc(val docs: List<ArticleSearch>) : Serializable {

    data class ArticleSearch(
        @SerializedName("abstract") var abstract: String? = null,
        @SerializedName("web_url") var webUrl: String? = null,
        @SerializedName("source") var source: String? = null,
        @SerializedName("headline") var headline: Headline? = null,
        @SerializedName("multimedia") var multimedia: ArrayList<Multimedia> = arrayListOf(),
        @SerializedName("_id") var Id: String? = null,
        @SerializedName("word_count") var wordCount: Int? = null,
        @SerializedName("uri") var uri: String? = null
    ) : Serializable
}

