package com.marahoney.tasque.di

import com.marahoney.tasque.data.local.DataRepository
import com.marahoney.tasque.data.local.DataRepositoryImpl
import com.marahoney.tasque.data.remote.NetworkApi
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object Modules {
    const val baseUrl = ""

    val apiModule: Module = module {
        single {
            Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(baseUrl)
                    .build()
                    .create(NetworkApi::class.java)
        }
    }

    val dataModule = module {
        single { DataRepositoryImpl(get()) as DataRepository }
    }
}