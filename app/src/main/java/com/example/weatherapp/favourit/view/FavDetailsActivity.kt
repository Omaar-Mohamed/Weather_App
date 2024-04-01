package com.example.weatherapp.favourit.view

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weatherapp.Home.view.DailyAdapter
import com.example.weatherapp.Home.view.HourlyAdapter
import com.example.weatherapp.Home.viewmodel.HomeViewModel
import com.example.weatherapp.Home.viewmodel.HomeViewModelFactory
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityFavDetailsBinding
import com.example.weatherapp.model.AppRepoImpl
import com.example.weatherapp.model.db.AppLocalDataSourseImpL
import com.example.weatherapp.model.dto.WeatherResponse
import com.example.weatherapp.model.network.ApiState
import com.example.weatherapp.model.network.AppRemoteDataSourseImpl
import com.example.weatherapp.shared.ApiConstants
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

class FavDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityFavDetailsBinding
    lateinit var activityIntent: Intent
    lateinit var homeviewModel: HomeViewModel
    lateinit var homeViewModelFactory: HomeViewModelFactory
    private lateinit var sharedPreferences: SharedPreferences
    private val temp = "metric"
    lateinit var hourlyAdapter: HourlyAdapter
    lateinit var dailyAdapter: DailyAdapter
    lateinit var dailyLayoutManager : LinearLayoutManager
    lateinit var hourlyLayoutManager : LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("weather_app", MODE_PRIVATE)
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
        val id = activityIntent.getIntExtra("id", 0)
        Toast.makeText(this, "id : $id", Toast.LENGTH_SHORT).show()
        homeViewModelFactory = HomeViewModelFactory(
            AppRepoImpl.getInstance(
                AppRemoteDataSourseImpl, AppLocalDataSourseImpL.getInstance(this)
            ),
            sharedPreferences
        )
        dailyLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        hourlyLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        hourlyAdapter = HourlyAdapter()
        dailyAdapter = DailyAdapter()
        binding.recyclerViewHourlyWeather.adapter = hourlyAdapter
        binding.recyclerViewDailyWeather.adapter = dailyAdapter
        binding.recyclerViewHourlyWeather.layoutManager = hourlyLayoutManager
        binding.recyclerViewDailyWeather.layoutManager = dailyLayoutManager
        homeviewModel = ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)
        homeviewModel.getWeather(
            lat.toString(),
            lon.toString(),
            ApiConstants.API_KEY,
            language = "en",
            units = "metric"
        )


        lifecycleScope.launch {
            homeviewModel.weather.collectLatest {
                when(it){
                    is ApiState.Loading -> {
                        // Handle loading state
                        // binding.progressBar.visibility = View.VISIBLE
                        // Hide other views
                        binding.textViewCurrentWeather.visibility = View.GONE
                        binding.cardViewCurrentWeather.visibility = View.GONE
                        binding.recyclerViewHourlyWeather.visibility = View.GONE
                        binding.recyclerViewDailyWeather.visibility = View.GONE
                        binding.textViewDailyWeather.visibility = View.GONE
                        binding.textViewHourlyWeather.visibility = View.GONE

// Show Lottie animation
                        binding.circularProgressBar.visibility = View.VISIBLE

                    }
                    is ApiState.Failure -> {
                        // Handle failure state
// Hide other views
                        binding.textViewCurrentWeather.visibility = View.VISIBLE
                        binding.cardViewCurrentWeather.visibility = View.VISIBLE
                        binding.recyclerViewHourlyWeather.visibility = View.VISIBLE
                        binding.recyclerViewDailyWeather.visibility = View.VISIBLE
                        binding.textViewDailyWeather.visibility = View.VISIBLE
                        binding.textViewHourlyWeather.visibility = View.VISIBLE



// Show Lottie animation
                        binding.circularProgressBar.visibility = View.GONE
                        val weatherResponse = homeviewModel.getWeatherResponseInFav(id.toString())

                        if (weatherResponse != null) {
                            updateUI(weatherResponse)
                        }


                        Log.i("response weather", "onCreateView:  error" + it.error)
                    }
                    is ApiState.Success -> {
                        // Handle success state
                        // binding.progressBar.visibility = View.GONE
                        binding.textViewCurrentWeather.visibility = View.VISIBLE
                        binding.cardViewCurrentWeather.visibility = View.VISIBLE
                        binding.recyclerViewHourlyWeather.visibility = View.VISIBLE
                        binding.recyclerViewDailyWeather.visibility = View.VISIBLE
                        binding.textViewDailyWeather.visibility = View.VISIBLE
                        binding.textViewHourlyWeather.visibility = View.VISIBLE

                        homeviewModel.saveWeatherResponseInFav(id.toString(), it.data)

// Show Lottie animation
                        binding.circularProgressBar.visibility = View.GONE
//                        Log.i("response weather", "onCreateView:  ${it.data}" )
//                        Log.i("response weather", "onCreateView:  ${it.data.hourly}" )
//                        Glide.with(requireContext()).load("https://openweathermap.org/img/wn/${it.data.current.weather[0].icon}.png").into(
//                        binding.imageViewWeatherIcon)
//                       binding.textViewWeatherDescription.text = splitTimeZone(it.data.timezone)
//                        binding.textViewTemperature.text = when(temp){
//                            "metric" -> it.data.current.temp.toString() + "°C"
//                            "imperial" -> it.data.current.temp.toString() + "°F"
//                            else -> it.data.current.temp.toString() + "°K"
//                        }
//                        binding.humidity.text = "${it.data.current.humidity?.toString() ?: ""}%"
//                        binding.clouds.text = "${it.data.current.clouds?.toString() ?: ""}%"
//                        binding.pressure.text = "${it.data.current.pressure?.toString() ?: ""}mb"
//                        binding.windy.text = "${it.data.current.windSpeed?.toString() ?: ""}%"
//
//                        hourlyAdapter.submitList(it.data.hourly)
//                        dailyAdapter.submitList(it.data.daily)
                        homeviewModel.saveWeatherResponse(it.data)
                        updateUI(it.data)

                    }

                    else -> {}
                }
            }
        }
    }
    fun splitTimeZone(timezone: String) : String{

        val parts = timezone.split("/")
        if (parts.size == 2) {
            val country = parts[0].trim()
            val city = parts[1].trim()
            // Now you can use `country` and `city` as needed
            return "$country, $city"
        } else {
            return ""
        }
    }
    private fun setLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
    private fun updateUI(weatherResponse: WeatherResponse) {
        // Update UI with the weather response data
        Log.i("response weather", "onCreateView:  ${weatherResponse}" )
        Log.i("response weather", "onCreateView:  ${weatherResponse.hourly}" )
        Glide.with(this).load("https://openweathermap.org/img/wn/${weatherResponse.current.weather[0].icon}.png").into(
            binding.imageViewWeatherIcon)
        binding.textViewWeatherDescription.text = splitTimeZone(weatherResponse.timezone)
        binding.textViewTemperature.text = when(temp){
            "metric" -> weatherResponse.current.temp.toString() + "°C"
            "imperial" -> weatherResponse.current.temp.toString() + "°F"
            else -> weatherResponse.current.temp.toString() + "°K"
        }
        binding.humidity.text = "${weatherResponse.current.humidity?.toString() ?: ""}%"
        binding.clouds.text = "${weatherResponse.current.clouds?.toString() ?: ""}%"
        binding.pressure.text = "${weatherResponse.current.pressure?.toString() ?: ""}mb"
        binding.windy.text = "${weatherResponse.current.windSpeed?.toString() ?: ""}%"

        hourlyAdapter.submitList(weatherResponse.hourly)
        dailyAdapter.submitList(weatherResponse.daily)
    }
}
