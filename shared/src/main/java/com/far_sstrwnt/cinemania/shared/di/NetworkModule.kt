package com.far_sstrwnt.cinemania.shared.di

import com.far_sstrwnt.cinemania.shared.data.datasource.remote.api.TmdbService
import com.far_sstrwnt.cinemania.shared.data.datasource.remote.api.TmdbService.Companion.API_KEY
import com.far_sstrwnt.cinemania.shared.data.datasource.remote.api.TmdbService.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideApi(retrofit: Retrofit): TmdbService {
        return retrofit.create(TmdbService::class.java)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val loggerInterceptor = HttpLoggingInterceptor()
        loggerInterceptor.level = HttpLoggingInterceptor.Level.BASIC

        val client = OkHttpClient.Builder()
            .addInterceptor(loggerInterceptor)
            .addInterceptor { chain ->
                val originalUrl = chain.request()
                    .url
                    .newBuilder()
                    .addQueryParameter("api_key", API_KEY)
                    .build()

                val request = chain.request()
                    .newBuilder()
                    .url(originalUrl)
                    .build()

                return@addInterceptor chain.proceed(request)
            }

        return client.build()
    }
}