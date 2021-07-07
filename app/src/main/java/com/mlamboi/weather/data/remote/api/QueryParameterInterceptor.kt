package com.mlamboi.weather.data.remote.api

import com.mlamboi.weather.utils.API_KEY
import com.mlamboi.weather.utils.UNIT
import okhttp3.Interceptor
import okhttp3.Response

class QueryParameterInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url().newBuilder()
            .addQueryParameter("appid", API_KEY)
            .addQueryParameter("units", UNIT)
            .build()

        val request = chain.request().newBuilder()
            .url(url)
            .build()

        return chain.proceed(request)
    }
}