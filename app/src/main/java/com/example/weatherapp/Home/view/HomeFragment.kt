package com.example.weatherapp.Home.view

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
import com.example.weatherapp.model.network.ApiState
import com.example.weatherapp.model.network.AppRemoteDataSourseImpl
import com.example.weatherapp.shared.ApiConstants
import com.example.weatherapp.shared.SharedViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    lateinit var homeviewModel: HomeViewModel
    lateinit var binding: FragmentHomeBinding
    lateinit var homeViewModelFactory: HomeViewModelFactory
    lateinit var myLayoutManager : LinearLayoutManager
    lateinit var hourlyAdapter: HourlyAdapter
    lateinit var dailyLayoutManager: LinearLayoutManager
    lateinit var dailyAdapter: DailyAdapter
    lateinit var sharedViewModel: SharedViewModel
     var language : String = "ar"

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
        homeViewModelFactory = HomeViewModelFactory(
            AppRepoImpl.getInstance(
                AppRemoteDataSourseImpl, AppLocalDataSourseImpL.getInstance(requireContext())

        ))
        // Initialize ViewModel
        homeviewModel = ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)

        // Start fetching weather data
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        lifecycleScope.launch {
            sharedViewModel.languageFlow.collectLatest {
//                Log.i("response weather", "onCreateView:  $it" )
                language = it
            }
        }

        if (ApiConstants.lat == null && ApiConstants.lon == null) {
            Toast.makeText(requireContext(), "Location not found", Toast.LENGTH_SHORT).show()
        }else {
            homeviewModel.getWeather(
                ApiConstants.lat!!,
                ApiConstants.lon!!,
                ApiConstants.API_KEY,
                language
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
                    }
                    is ApiState.Failure -> {
                        // Handle failure state
                        // binding.progressBar.visibility = View.GONE
                        Log.i("response weather", "onCreateView:  error" + it.error)
                    }
                    is ApiState.Success -> {
                        // Handle success state
                        // binding.progressBar.visibility = View.GONE
                        Log.i("response weather", "onCreateView:  ${it.data}" )
                        Log.i("response weather", "onCreateView:  ${it.data.hourly}" )
                        Glide.with(requireContext()).load("https://openweathermap.org/img/wn/${it.data.current.weather[0].icon}.png").into(
                        binding.imageViewWeatherIcon)
                       binding.textViewWeatherDescription.text = splitTimeZone(it.data.timezone)
                        binding.textViewTemperature.text = kelvinToCelsius(it.data.current.temp)
                        hourlyAdapter.submitList(it.data.hourly)
                        dailyAdapter.submitList(it.data.daily)

                    }
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
}
