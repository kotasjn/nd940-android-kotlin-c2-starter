package com.udacity.asteroidradar.api

import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.AsteroidRadarApplication
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.Constants.BASE_URL
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.QueryMap

enum class AsteroidFilter {
    WEEK,
    TODAY,
    ALL
}

interface ApiService {

    @GET("neo/rest/v1/feed")
    fun getAsteroids(
        @QueryMap(encoded = true) options: Map<String, String>
    ): Deferred<String>

    @GET("planetary/apod")
    fun getPictureOfDay(): Deferred<PictureOfDayDto>

}

/**
 * Interceptor that adds NASA's api key to each service call.
 */
class CustomInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url()
            .newBuilder()
            .addQueryParameter("api_key", BuildConfig.NASA_API_KEY)
            .build()
        return chain.proceed(
            chain.request()
                .newBuilder()
                .url(url)
                .build()
        )
    }
}

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(CustomInterceptor())
    .addInterceptor(
        ChuckerInterceptor.Builder(AsteroidRadarApplication.appContext)
            .collector(ChuckerCollector(AsteroidRadarApplication.appContext))
            .maxContentLength(250000L)
            .redactHeaders(emptySet())
            .alwaysReadResponseBody(false)
            .build()
    )
    .build()

// Configure retrofit to parse JSON and use coroutines
private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .client(okHttpClient)
    .build()

/**
 * Main entry point for network access.
 */
object NetworkApi {
    val service: ApiService = retrofit.create(ApiService::class.java)
}