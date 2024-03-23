package com.example.weatherapp.favourit.view

import android.content.Intent
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.R
import com.example.weatherapp.favourit.viewmodel.FavouritViewModel
import com.example.weatherapp.favourit.viewmodel.FavouritViewModelFactory
import com.example.weatherapp.model.AppRepoImpl
import com.example.weatherapp.model.db.AppLocalDataSourseImpL
import com.example.weatherapp.model.dto.FavLocations
import com.example.weatherapp.model.network.AppRemoteDataSourseImpl
import com.example.weatherapp.shared.ApiConstants
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
    lateinit var favouritViewModel: FavouritViewModel
    lateinit var viewModelFactory: FavouritViewModelFactory
    lateinit var Myintent: Intent
    lateinit var extraValue: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        map = findViewById(R.id.map)
        Myintent = intent
         extraValue = Myintent.getStringExtra("request_fragment")?:""
        val supportMapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        supportMapFragment.getMapAsync(this)

        viewModelFactory = FavouritViewModelFactory(
            AppRepoImpl.getInstance(
                AppRemoteDataSourseImpl, AppLocalDataSourseImpL.getInstance(this)
            )
        )
        favouritViewModel = ViewModelProvider(this, viewModelFactory).get(FavouritViewModel::class.java)

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
                   showDialoge( FavLocations(  latLng.latitude, latLng.longitude , addressString))


                } else {
                    Log.i("mapInfo", "No address found for the provided coordinates")
                }
            } catch (e: IOException) {
                Log.e("mapInfo", "Error getting address: ${e.message}")
            }
        }
    }

    fun showDialoge( location: FavLocations){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Add Place as Favorite")
        builder.setMessage("Do you want to add this place as a favorite?")
        builder.setPositiveButton("Yes") { _, _ ->
            // Insert the location into the Room database
            if (extraValue == "Alert"){
                Toast.makeText(this, "Location added to Alarm", Toast.LENGTH_SHORT).show()
//                ApiConstants.alertLocation = location.address
                val SharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
                val myEdit = SharedPreferences.edit()
                myEdit.putString("alertLocation", location.address)
                myEdit.apply()
            }
            else {
                favouritViewModel.insertLocation(location)
                Toast.makeText(this, "Location added to favorites", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()



    }

}