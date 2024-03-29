package com.example.weatherapp


import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
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
import androidx.core.view.ViewCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.shared.ApiConstants
import com.example.weatherapp.shared.SharedViewModel
import com.google.android.gms.common.api.Api
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    lateinit var fusedClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    private var hasNavigatedToHome = false // Flag to track navigation to HomeFragment
    lateinit var sharedViewModel: SharedViewModel



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
        sharedViewModel = ViewModelProvider(this@MainActivity).get(SharedViewModel::class.java)
        sharedViewModel.setLanguage(ApiConstants.getSelectedLanguage(this@MainActivity))
        lifecycleScope.launch {
            sharedViewModel.languageFlow.collect { language ->
                // Set locale and layout direction based on selected language
                if (language == "en") {
                    setLocale("en")
                    drawerLayout.layoutDirection = DrawerLayout.LAYOUT_DIRECTION_LTR
                    updateDrawerMenuItems("en")
                } else {
                    setLocale("ar")
                    drawerLayout.layoutDirection = DrawerLayout.LAYOUT_DIRECTION_RTL
                    updateDrawerMenuItems("ar")
                }

                // Update activity title based on selected language
                val appName = getString(R.string.app_name)
                title = if (language == "en") {
                    appName
                } else {
                    // For Arabic or other languages, load the localized app name from resources
                    val configuration = resources.configuration
                    configuration.setLocale(Locale(language))
                    val localizedAppName = createConfigurationContext(configuration).getString(R.string.app_name)
                    localizedAppName
                }
            }
        }

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

    private fun setLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = Configuration(resources.configuration)
        configuration.setLocale(locale)
        val context = applicationContext.createConfigurationContext(configuration)

    }

    private fun updateDrawerMenuItems(languageCode: String) {
        val navView = findViewById<NavigationView>(R.id.nav_view)
        val menu = navView.menu
        // Loop through each item in the menu and update its title
        for (i in 0 until menu.size()) {
            val item = menu.getItem(i)
            when (item.itemId) {
                R.id.homeFragment -> {
                    item.title = getLocalizedString(R.string.home, languageCode)
                }
                R.id.favouritFragment -> {
                    item.title = getLocalizedString(R.string.favourit, languageCode)
                }
                R.id.alertFragment -> {
                    item.title = getLocalizedString(R.string.alert, languageCode)
                }
                R.id.settingsFragment -> {
                    item.title = getLocalizedString(R.string.settings, languageCode)
                }
                // Add cases for other menu items as needed
            }
        }
    }

    // Utility function to get localized string based on language code
    private fun getLocalizedString(stringResId: Int, languageCode: String): String {
        val locale = Locale(languageCode)
        val configuration = Configuration(resources.configuration)
        configuration.setLocale(locale)
        val localizedContext = createConfigurationContext(configuration)
        return localizedContext.getString(stringResId)
    }

    }
