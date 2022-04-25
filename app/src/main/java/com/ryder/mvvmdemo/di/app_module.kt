package com.ryder.mvvmdemo.di

import com.ryder.mvvmdemo.data.remote.AssetService
import com.ryder.mvvmdemo.data.repository.AssetsRepository
import com.ryder.mvvmdemo.ui.list.AssetsViewModel
import com.ryder.mvvmdemo.util.OPEN_SEA_BASE_URL
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val remoteDataSourceModule = module {
    single {
        Retrofit.Builder()
            .baseUrl(OPEN_SEA_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single { get<Retrofit>().create(AssetService::class.java) }
}

val assetsModule = module {
    single {
        AssetsRepository(get())
    }
    viewModel { AssetsViewModel(get()) }
}