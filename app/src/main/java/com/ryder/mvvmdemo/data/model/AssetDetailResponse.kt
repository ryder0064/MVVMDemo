package com.ryder.mvvmdemo.data.model

import com.google.gson.annotations.SerializedName

data class AssetDetailResponse(
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("permalink")
    val permalink: String
)