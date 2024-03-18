package com.example.weatherapp.favourit.view

import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import com.example.weatherapp.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.Locale

class MapActivity : FragmentActivity() , OnMapReadyCallback {

    lateinit var gMap: GoogleMap
    lateinit var map: FrameLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        map = findViewById(R.id.map)
        val supportMapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        supportMapFragment.getMapAsync(this)

    }

    override fun onMapReady(map: GoogleMap) {
        gMap = map
        // Set a default location (e.g., your current location or a specific location)
        val defaultLocation = LatLng(30.203409207590017, 31.17056585848331)
        gMap.addMarker(MarkerOptions().position(defaultLocation).title("Marker in San Francisco"))
        gMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation))

        // Set a listener to handle map clicks
        gMap.setOnMapClickListener { latLng ->
            // Handle map click
            gMap.clear() // Clear existing markers
            gMap.addMarker(MarkerOptions().position(latLng).title("Selected Location"))

            // Use Geocoder to retrieve address information
            val geocoder = Geocoder(this, Locale.getDefault())
            try {
                val addresses: List<Address>? = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                if (!addresses.isNullOrEmpty()) {
                    val address: Address = addresses[0]
                    val addressString = address.getAddressLine(0) // Get the first line of the address
                    Log.i("mapInfo", "Address: $addressString")
                    // Now you can use the addressString as needed, such as displaying it in a TextView
                } else {
                    Log.i("mapInfo", "No address found for the provided coordinates")
                }
            } catch (e: IOException) {
                Log.e("mapInfo", "Error getting address: ${e.message}")
            }
        }
    }

}