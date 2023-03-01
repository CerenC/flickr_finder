package com.cerenb.flickrfinder.di

import com.cerenb.flickrfinder.data.datasources.remote.interceptor.AuthenticationInterceptor
import com.cerenb.flickrfinder.data.datasources.remote.service.PhotoSearchService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Singleton
    @Provides
    fun provideNetworkInterceptor(): AuthenticationInterceptor = AuthenticationInterceptor()

    @Singleton
    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        authenticationInterceptor: AuthenticationInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(READ_TIMEOUT_SEC, TimeUnit.SECONDS)
            .connectTimeout(CONNECT_TIMEOUT_SEC, TimeUnit.SECONDS)
            .addInterceptor(authenticationInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun providePhotoSearchService(retrofit: Retrofit): PhotoSearchService {
        return retrofit.create(PhotoSearchService::class.java)
    }

    companion object {
        private const val BASE_URL = "https://www.flickr.com/services/rest/"
        private const val READ_TIMEOUT_SEC = 30L
        private const val CONNECT_TIMEOUT_SEC = 30L
    }


}