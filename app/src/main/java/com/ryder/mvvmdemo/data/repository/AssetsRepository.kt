package com.ryder.mvvmdemo.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ryder.mvvmdemo.data.model.AssetDetailResponse
import com.ryder.mvvmdemo.data.model.AssetListResponse
import com.ryder.mvvmdemo.data.remote.AssetService
import com.ryder.mvvmdemo.util.LIMIT
import com.ryder.mvvmdemo.util.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AssetsRepository(private val assetService: AssetService) {

    private val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    private val snackBarMessage = MutableLiveData<String>()
    private val assetList = MutableLiveData<List<AssetListResponse.Asset>>()
    private val assetDetail = MutableLiveData<AssetDetailResponse>()

    fun getLoadingStatus(): LiveData<Boolean> = isLoading
    fun getSnackBarMessage(): MutableLiveData<String> = snackBarMessage
    fun getAssetList(): LiveData<List<AssetListResponse.Asset>> = assetList
    fun getAssetDetail(): LiveData<AssetDetailResponse> = assetDetail

    suspend fun refreshAssetList(offset: Int) {
        isLoading.postValue(true)
        withContext(Dispatchers.IO) {
            val response =
                assetService.getAssetList("0x960DE9907A2e2f5363646d48D7FB675Cd2892e91", offset, LIMIT)

            if (response.isSuccessful) {
                response.body()?.let {
                    assetList.postValue(it.assets)
                }
            } else {
                snackBarMessage.postValue("Unexpected problem occurred")
            }
            isLoading.postValue(false)
        }
    }

    suspend fun refreshAssetDetail(contractAddress: String, tokenId: String) {
        isLoading.postValue(true)
        withContext(Dispatchers.IO) {
            val response = assetService.getAssetDetail(contractAddress, tokenId)
            if (response.isSuccessful) {
                response.body()?.let {
                    assetDetail.postValue(it)
                }
            } else {
                snackBarMessage.postValue("Unexpected problem occurred")
            }
            isLoading.postValue(false)
        }
    }
}