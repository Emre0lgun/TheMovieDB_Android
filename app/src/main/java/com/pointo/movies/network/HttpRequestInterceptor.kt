package com.pointo.movies.network

import android.content.Context
import com.pointo.movies.BuildConfig
import com.pointo.movies.util.isConnected
import okhttp3.Interceptor
import okhttp3.Response

internal class HttpRequestInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest = chain.request()
        val originalUrl = originalRequest.url()
        val url = originalUrl.newBuilder()
            .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
            .build()

        val requestBuilder = originalRequest.newBuilder().url(url)
        if (context.isConnected()) {
            /* If there is Internet, get the cache that was stored 5 seconds ago.
             *  If the cache is older than 5 seconds, then discard it,
             *  and indicate an error in fetching the response.
             *  The 'max-age' attribute is responsible for this behavior.   */
            requestBuilder.header(
                "Cache-Control",
                "public, max-age=" + 5
            ).build()
        } else {
            /*  If there is no Internet, get the cache that was stored 7 days ago.
             *  If the cache is older than 7 days, then discard it,
             *  and indicate an error in fetching the response.
             *  The 'max-stale' attribute is responsible for this behavior.
             *  The 'only-if-cached' attribute indicates to not retrieve new data; fetch the cache only instead.    */
            requestBuilder.header(
                "Cache-Control",
                "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
            ).build()

        }
        val request = requestBuilder.build()

        return chain.proceed(request)
    }
}
