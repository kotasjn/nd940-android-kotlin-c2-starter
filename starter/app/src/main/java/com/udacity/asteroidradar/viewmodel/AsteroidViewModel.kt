package com.udacity.asteroidradar.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class AsteroidViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)

    private val asteroidsRepository = AsteroidRepository(database)

    val pictureOfDay = asteroidsRepository.pictureOFDay

    val asteroids = asteroidsRepository.asteroids

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid?>()
    val navigateToSelectedAsteroid: MutableLiveData<Asteroid?>
        get() = _navigateToSelectedAsteroid

    init {
        viewModelScope.launch {
            try {
                asteroidsRepository.getPictureOfDay()
                asteroidsRepository.refreshAsteroids()
            } catch (e: Exception) {
                Log.e("AsteroidRadar", e.toString())
            }
        }
    }

    fun displayAsteroidDetail(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayPropertyDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }

    /**
     * Factory for constructing AsteroidViewModel with parameter
     */
    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AsteroidViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AsteroidViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}