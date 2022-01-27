package com.pomeloassignment.android.model

import com.google.gson.annotations.SerializedName

data class Article(
    @SerializedName("uri") val uri: String,
    @SerializedName("url") val url: String,
    @SerializedName("id") val id: Long,
    @SerializedName("asset_id") val assetId: Long,
    @SerializedName("source") val source: String,
    @SerializedName("published_date") val publishedDate: String,
    @SerializedName("updated") val updated: String,
    @SerializedName("section") val section: String,
    @SerializedName("subsection") val subsection: String,
    @SerializedName("nytdsection") val nytdsection: String,
    @SerializedName("type") val type: String,
    @SerializedName("title") val title: String,
    @SerializedName("abstract") val abstract: String,
    @SerializedName("media") val media: List<Media>,
    @SerializedName("eta_id") val etaId: Long
)