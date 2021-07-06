package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Constants.DEFAULT_END_DATE_DAYS
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.domain.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository(private val database: AsteroidDatabase) {

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOFDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAllAsteroids()) {
            it.asDomainModel()
        }

    private val queryMap = QueryMap<String, String>()

    init {
        queryMap["start_date"] = getDateFormatted()
        queryMap["end_date"] = getDateFormatted(DEFAULT_END_DATE_DAYS)
    }

    suspend fun getPictureOfDay() {
        withContext(Dispatchers.IO) {
            val pictureOfDayDto = NetworkApi.service.getPictureOfDay().await()
            _pictureOfDay.postValue(pictureOfDayDto.asDomainModel())
        }
    }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val asteroids = NetworkApi.service.getAsteroids(queryMap).await()
            database.asteroidDao.insertAll(*parseAsteroidsJsonResult(JSONObject(asteroids)).asDatabaseModel())
        }
    }

}