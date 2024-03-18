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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    lateinit var homeviewModel: HomeViewModel
    lateinit var binding: FragmentHomeBinding
    lateinit var homeViewModelFactory: HomeViewModelFactory
    lateinit var myLayoutManager : LinearLayoutManager
    lateinit var hourlyAdapter: HourlyAdapter

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
        myLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewHourlyWeather.layoutManager = myLayoutManager

        binding.recyclerViewHourlyWeather.adapter = hourlyAdapter
        homeViewModelFactory = HomeViewModelFactory(
            AppRepoImpl.getInstance(
                AppRemoteDataSourseImpl, AppLocalDataSourseImpL

        ))
        // Initialize ViewModel
        homeviewModel = ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)

        // Start fetching weather data

        if (ApiConstants.lat == null && ApiConstants.lon == null) {
            Toast.makeText(requireContext(), "Location not found", Toast.LENGTH_SHORT).show()
        }else {
            homeviewModel.getWeather(
                ApiConstants.lat!!,
                ApiConstants.lon!!,
                ApiConstants.API_KEY
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
                        Log.i("response weather", "onCreateView:  ${it.data.lat}" )
                        Log.i("response weather", "onCreateView:  ${it.data.hourly}" )
                        hourlyAdapter.submitList(it.data.hourly)

                    }
                }
            }
        }
    }
}
