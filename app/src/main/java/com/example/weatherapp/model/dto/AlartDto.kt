package com.example.weatherapp.model.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "alerts")
data class AlertDto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val location: String,
    val alertDate: String,
    val alertTime: String,
)
