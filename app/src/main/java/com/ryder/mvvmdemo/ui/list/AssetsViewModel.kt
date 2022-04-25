package com.ryder.mvvmdemo.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ryder.mvvmdemo.data.model.AssetDetailResponse
import com.ryder.mvvmdemo.data.model.AssetListResponse
import com.ryder.mvvmdemo.data.repository.AssetsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AssetsViewModel(
    private val repository: AssetsRepository
) : ViewModel() {

    private val _assetList: LiveData<List<AssetListResponse.Asset>> = repository.getAssetList()
    private val _assetDetail: LiveData<AssetDetailResponse> = repository.getAssetDetail()
    private val _isLoading: LiveData<Boolean> = repository.getLoadingStatus()
    private val _snackBarMessage: MutableLiveData<String> = repository.getSnackBarMessage()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.refreshAssetList(0)
        }
    }

    fun getSnackBarMessage() = _snackBarMessage
    fun getAssetList(): LiveData<List<AssetListResponse.Asset>> = _assetList
    fun getAssetDetail(): LiveData<AssetDetailResponse> = _assetDetail
    fun getLoadingStatus(): LiveData<Boolean> = _isLoading
    fun refreshAssetList(offset: Int = 0) = viewModelScope.launch { repository.refreshAssetList(offset) }
    fun refreshAssetDetail(contractAddress: String, tokenId: String) =
        viewModelScope.launch { repository.refreshAssetDetail(contractAddress, tokenId) }

    fun clearSnackBar() {
        _snackBarMessage.value = ""
    }
}
