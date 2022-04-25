package com.ryder.mvvmdemo.data.remote

import com.ryder.mvvmdemo.data.model.AssetDetailResponse
import com.ryder.mvvmdemo.data.model.AssetListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface AssetService {
    @Headers(value = ["X-API-KEY: 5b294e9193d240e39eefc5e6e551ce83"])
    @GET("v1/assets")
    suspend fun getAssetList(
        @Query("owner") owner: String,
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int
    ): Response<AssetListResponse>

    @Headers(value = ["X-API-KEY: 5b294e9193d240e39eefc5e6e551ce83"])
    @GET("v1/asset/{contract_address}/{token_id}")
    suspend fun getAssetDetail(
        @Path("contract_address") contractAddress: String,
        @Path("token_id") tokenId: String
    ): Response<AssetDetailResponse>
}