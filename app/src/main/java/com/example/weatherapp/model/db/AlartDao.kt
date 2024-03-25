package com.example.weatherapp.model.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.model.dto.AlertDto
import kotlinx.coroutines.flow.Flow

@Dao
interface AlartDao {
    @Query("SELECT * FROM alerts")
    fun getAllAlarts(): Flow<List<AlertDto>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlart(alart: AlertDto): Long

    @Delete
    suspend fun deleteAlart(alart: AlertDto)

    @Query("SELECT * FROM alerts WHERE id = (SELECT MAX(id) FROM alerts)")
     fun getLastInsertedRow(): Flow<AlertDto?>

    @Query("DELETE FROM alerts WHERE id = :alertId")
    suspend fun deleteAlartById(alertId: Long)
}