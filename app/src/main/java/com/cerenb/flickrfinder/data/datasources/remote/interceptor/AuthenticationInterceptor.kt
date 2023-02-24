package com.cerenb.flickrfinder.data.datasources.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class AuthenticationInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val url = originalRequest.url.newBuilder()
            .addQueryParameter(API_KEY, API_KEY_VALUE)
            .build()

        val request = originalRequest.newBuilder().url(url).build()
        return chain.proceed(request)
    }

    companion object {
        const val API_KEY = "api_key"
        const val API_KEY_VALUE = "1508443e49213ff84d566777dc211f2a"
    }

}