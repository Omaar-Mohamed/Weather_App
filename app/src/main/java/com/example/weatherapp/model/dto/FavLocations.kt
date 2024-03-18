package com.example.weatherapp.model.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_location_table")
data class FavLocations(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val latitude: Double,
    val longitude: Double,
    val address: String
) {
    constructor(latitude: Double, longitude: Double, address: String) : this(0, latitude, longitude, address)
}
