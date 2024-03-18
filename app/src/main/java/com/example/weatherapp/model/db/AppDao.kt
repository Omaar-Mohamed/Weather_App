package com.example.weatherapp.model.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.model.dto.FavLocations
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Query("SELECT * FROM fav_location_table")
    fun getAll(): Flow<List<FavLocations>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(locations: FavLocations): Long

    @Delete
    suspend fun delete(location: FavLocations)


}