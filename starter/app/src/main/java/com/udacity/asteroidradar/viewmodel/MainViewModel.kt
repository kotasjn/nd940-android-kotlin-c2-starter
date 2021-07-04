package com.udacity.asteroidradar.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay

class MainViewModel : ViewModel() {

    private val _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid?>()
    val navigateToSelectedAsteroid: MutableLiveData<Asteroid?>
        get() = _navigateToSelectedAsteroid

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
    get() = _pictureOfDay

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

    fun displayAsteroidDetail(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayPropertyDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }

}