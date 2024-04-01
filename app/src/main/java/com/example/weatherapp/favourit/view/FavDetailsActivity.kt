package com.example.weatherapp.favourit.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.weatherapp.R

class FavDetailsActivity : AppCompatActivity() {
    lateinit var binding: FavDetailsActivity
    lateinit var activityIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fav_details)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Favourite Details"

        // Handle back button click
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        activityIntent = getIntent() // Updated variable name
        val lat = activityIntent.getFloatExtra("lat", 0.0f)
        val lon = activityIntent.getFloatExtra("lon", 0.0f)
        Toast.makeText(this, "Latitude: $lat, Longitude: $lon", Toast.LENGTH_SHORT).show()
    }
}
