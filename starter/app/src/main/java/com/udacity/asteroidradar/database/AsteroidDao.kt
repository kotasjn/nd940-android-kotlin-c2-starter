package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidDao {

    @Query("select * from asteroid where closeApproachDate between :startDate and :endDate order by closeApproachDate asc")
    fun getWeekAsteroids(startDate: String, endDate: String): LiveData<List<AsteroidEntity>>

    @Query("select * from asteroid where closeApproachDate = :date order by closeApproachDate asc")
    fun getTodayAsteroids(date: String): LiveData<List<AsteroidEntity>>

    @Query("select * from asteroid order by closeApproachDate asc")
    fun getAllAsteroids(): LiveData<List<AsteroidEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: AsteroidEntity)
}