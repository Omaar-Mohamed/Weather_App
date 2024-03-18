package com.example.weatherapp


import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.shared.ApiConstants
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    lateinit var fusedClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    private var hasNavigatedToHome = false // Flag to track navigation to HomeFragment



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        fusedClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.Builder(1000).apply {
            setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        }.build()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
        } else {
            startLocationUpdates()
        }




        navController = findNavController(R.id.nav_host_fragment)

        setSupportActionBar(binding.toolbar)

        binding.toolbar.setNavigationIcon(R.drawable.icons8_menu)

        drawerLayout = binding.drawerLayout
        navigationView = binding.navView
        binding.toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(navigationView)
        }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                    closeDrawer()
                }

                R.id.favouritFragment -> {
                    navController.navigate(R.id.favouritFragment)
                    closeDrawer()
                }

                R.id.alertFragment -> {
                    navController.navigate(R.id.alertFragment)
                    closeDrawer()
                }

                R.id.settingsFragment -> {
                    navController.navigate(R.id.settingsFragment)
                    closeDrawer()
                }
            }
            true
        }
    }
        private fun closeDrawer() {
            drawerLayout.closeDrawer(GravityCompat.START)
        }


    private fun startLocationUpdates() {
        val callBack = object : LocationCallback() {
            @RequiresApi(Build.VERSION_CODES.TIRAMISU)
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                val location = p0.lastLocation
//                langtv.text = location?.longitude.toString()
//                lattv.text = location?.latitude.toString()
                    // Navigate to the desired fragment (e.g., HomeFragment)
                if (!hasNavigatedToHome) {
                    // Update ApiConstants.lat and ApiConstants.lon
                    ApiConstants.lat = location?.latitude.toString()
                    ApiConstants.lon = location?.longitude.toString()

                    // Navigate to HomeFragment
                    navController.navigate(R.id.homeFragment)

                    // Set flag to true to prevent multiple navigations
                    hasNavigatedToHome = true
                }

                // Additional code to handle retrieving the address and updating ApiConstants.address



                val geo: Geocoder = Geocoder(this@MainActivity)
                location?.let {
                    val addresses: List<Address>? = geo.getFromLocation(
                        it.latitude ?: 0.0,
                        it.longitude ?: 0.0,
                        1
                    ) ?: emptyList()

                    if (addresses!!.isNotEmpty()) {
                        val addressString =
                            "Country: ${addresses[0].countryName}\nadmin:${addresses[0].adminArea}\nStreet: ${addresses[0].getAddressLine(0)}"
//                        address.text = addressString
                        ApiConstants.address = addressString

                    }
                }


            }
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedClient.requestLocationUpdates(locationRequest, callBack, Looper.myLooper())
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates()
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }
    }
