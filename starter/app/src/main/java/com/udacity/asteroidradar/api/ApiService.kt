package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.BuildConfig

private const val BASE_URL = "https://api.nasa.gov/planetary/apod?api_key=" + BuildConfig.NASA_API_KEY


interface ApiService