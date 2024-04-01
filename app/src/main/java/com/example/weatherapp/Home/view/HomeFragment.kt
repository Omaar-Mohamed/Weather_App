package com.example.weatherapp.Home.view

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weatherapp.Home.viewmodel.HomeViewModel
import com.example.weatherapp.Home.viewmodel.HomeViewModelFactory
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.example.weatherapp.model.AppRepo
import com.example.weatherapp.model.AppRepoImpl
import com.example.weatherapp.model.db.AppLocalDataSourse
import com.example.weatherapp.model.db.AppLocalDataSourseImpL
import com.example.weatherapp.model.dto.WeatherResponse
import com.example.weatherapp.model.network.ApiState
import com.example.weatherapp.model.network.AppRemoteDataSourseImpl
import com.example.weatherapp.shared.ApiConstants
import com.example.weatherapp.shared.SharedViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale


class HomeFragment : Fragment() {

    lateinit var homeviewModel: HomeViewModel
    lateinit var binding: FragmentHomeBinding
    lateinit var homeViewModelFactory: HomeViewModelFactory
    lateinit var myLayoutManager : LinearLayoutManager
    lateinit var hourlyAdapter: HourlyAdapter
    lateinit var dailyLayoutManager: LinearLayoutManager
    lateinit var dailyAdapter: DailyAdapter
    lateinit var sharedViewModel: SharedViewModel
    private lateinit var sharedPreferences: SharedPreferences

    var language : String = "ar"
    var temp : String = "metric"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hourlyAdapter = HourlyAdapter()
        dailyAdapter = DailyAdapter()
        myLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        dailyLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewHourlyWeather.layoutManager = myLayoutManager
        binding.recyclerViewDailyWeather.layoutManager = dailyLayoutManager

        binding.recyclerViewHourlyWeather.adapter = hourlyAdapter
        binding.recyclerViewDailyWeather.adapter = dailyAdapter
        sharedPreferences = requireActivity().getSharedPreferences("Your_Pref_Name", Context.MODE_PRIVATE)
        homeViewModelFactory = HomeViewModelFactory(
            AppRepoImpl.getInstance(
                AppRemoteDataSourseImpl, AppLocalDataSourseImpL.getInstance(requireContext())
        ),
            sharedPreferences
        )
        // Initialize ViewModel
        homeviewModel = ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)

        // Start fetching weather data
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        lifecycleScope.launch {
            sharedViewModel.languageFlow.collectLatest {
//                Log.i("response weather", "onCreateView:  $it" )
                language = it
                if (language == "ar") {
                    setLocale("ar")
                } else {
                    setLocale("en")
                }
            }
        }
        temp = ApiConstants.getSelectedDegree(requireContext())
        lifecycleScope.launch{
            sharedViewModel.degreeFlow.collectLatest {
                temp = it
            }
        }

        if (ApiConstants.lat == null && ApiConstants.lon == null) {
            Toast.makeText(requireContext(), "Location not found", Toast.LENGTH_SHORT).show()
        }else {
            homeviewModel.getWeather(
                ApiConstants.lat!!,
                ApiConstants.lon!!,
                ApiConstants.API_KEY,
                language,
                temp,
            )
//            Toast.makeText(requireContext(), ApiConstants.address, Toast.LENGTH_SHORT).show()
        }
        // Observe the weather data
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
                        val weatherResponse = homeviewModel.getWeatherResponse()

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

    fun kelvinToCelsius(kelvin: Double): String {
        val celsius = kelvin - 273.15
        return String.format("%.1f°C", celsius)
    }
    fun fehToCelsius(feh: Double): String {
        var celsius = (feh - 32) * 5 / 9
        return celsius . toString() + "°C"
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
        Glide.with(requireContext()).load("https://openweathermap.org/img/wn/${weatherResponse.current.weather[0].icon}.png").into(
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
