package com.luiz.reminder.di

import android.util.Log
import com.luiz.reminder.api.services.ApiDataSource
import com.luiz.reminder.api.constants.Constants
import com.luiz.reminder.api.services.ApiRepository
import com.luiz.reminder.util.Utils
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
open class ApiModule {

    private val CONNECTION_TIMEOUT = 20 * 1000

    private val client = OkHttpClient.Builder().addInterceptor { chain ->
        var newRequest = chain.request()

        if (Utils.getApiToken() != null) {
            newRequest = chain.request().newBuilder()
                .header("Content-Type","application/json")
                .addHeader("Authorization", "Bearer " + Utils.getApiToken())
                .build()
        }
        Log.d("_res", "Response: " + newRequest.toString())

        chain.proceed(newRequest)
    }.connectTimeout(CONNECTION_TIMEOUT.toLong(), TimeUnit.MINUTES)
        .readTimeout(1, TimeUnit.MINUTES).build()

    @Provides
    fun provideApi(): ApiDataSource {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(ApiDataSource::class.java)
    }

    @Provides
    open fun providesService(): ApiRepository {
        return ApiRepository()
    }
}