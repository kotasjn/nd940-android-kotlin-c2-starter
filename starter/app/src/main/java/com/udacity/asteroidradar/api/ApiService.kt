package com.udacity.asteroidradar.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.domain.PictureOfDay
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("neo/rest/v1/feed")
    fun getAsteroids(
        @Query("start_date") startDay: String,
        @Query("end_date") endDay: String
    ): Deferred<JSONObject>

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

/**
 * Main entry point for network access.
 */
object NetworkApi {

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(CustomInterceptor())
        .build()

    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(okHttpClient)
        .build()

    val service: ApiService = retrofit.create(ApiService::class.java)
}