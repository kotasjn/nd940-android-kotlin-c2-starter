package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.api.NetworkApi
import com.udacity.asteroidradar.api.asDomainModel
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AsteroidsRepository(private val database: AsteroidsDatabase) {

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOFDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    private val _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids

    init {
        val list = mutableListOf<Asteroid>()
        list.add(
            Asteroid(
                1,
                "68347 (2001 KB67)",
                "2020-02-08",
                19.9,
                0.622358,
                15.515735,
                0.445338,
                true
            )
        )
        _asteroids.value = list
    }

    suspend fun getPictureOfDay() {
        withContext(Dispatchers.IO) {
            val pictureOfDayDto = NetworkApi.service.getPictureOfDay().await()
            _pictureOfDay.postValue(pictureOfDayDto.asDomainModel())
        }
    }

}