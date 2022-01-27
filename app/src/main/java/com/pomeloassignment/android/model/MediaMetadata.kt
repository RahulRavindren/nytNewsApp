package com.pomeloassignment.android.model

import com.google.gson.annotations.SerializedName

data class MediaMetadata(
    @SerializedName("url") val url: String,
    @SerializedName("format") val format: String,
    @SerializedName("height") val height: Int,
    @SerializedName("width") val width: Int
)