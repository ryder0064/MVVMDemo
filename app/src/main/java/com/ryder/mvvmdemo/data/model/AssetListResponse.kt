package com.ryder.mvvmdemo.data.model

import com.google.gson.annotations.SerializedName

data class AssetListResponse(
    @SerializedName("assets")
    val assets: List<Asset>,
) {
    data class Asset(
        @SerializedName("image_url")
        val imageUrl: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("token_id")
        val tokenId: String,
        @SerializedName("permalink")
        val permalink: String,
        @SerializedName("asset_contract")
        val assetContract: AssetContract
    ) {
        data class AssetContract(
            @SerializedName("address")
            val address: String
        )
    }
}