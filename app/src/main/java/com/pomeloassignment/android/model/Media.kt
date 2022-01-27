package com.pomeloassignment.android.model

import com.google.gson.annotations.SerializedName

data class Media(
    @SerializedName("type") val type: String,
    @SerializedName("subtype") val subtype: String,
    @SerializedName("caption") val caption: String,
    @SerializedName("copyright") val copyright: String,
    @SerializedName("media-metadata") val mediaMetadata: List<MediaMetadata>
)