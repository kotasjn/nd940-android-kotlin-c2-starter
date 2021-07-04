package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Asteroid

class MainViewModel : ViewModel() {

    private val _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids

    var list = mutableListOf<Asteroid>()

    init {
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

    }

}