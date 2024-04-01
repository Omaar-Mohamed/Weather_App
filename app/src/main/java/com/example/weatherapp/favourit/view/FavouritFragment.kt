package com.example.weatherapp.favourit.view

import android.content.Intent
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
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentFavouritBinding
import com.example.weatherapp.favourit.viewmodel.FavouritViewModel
import com.example.weatherapp.favourit.viewmodel.FavouritViewModelFactory
import com.example.weatherapp.model.AppRepoImpl
import com.example.weatherapp.model.db.AppLocalDataSourseImpL
import com.example.weatherapp.model.db.DbState
import com.example.weatherapp.model.network.AppRemoteDataSourseImpl
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class FavouritFragment : Fragment() {
    lateinit var binding: FragmentFavouritBinding
    lateinit var favAdapter: FavAdapter
    lateinit var favLayoutManager: LinearLayoutManager
    lateinit var favViewModelFactory: FavouritViewModelFactory
    lateinit var favViewModel: FavouritViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavouritBinding.bind(view)
        binding.fab.setOnClickListener {
            var intent = Intent(requireContext() , MapActivity::class.java)
            startActivity(intent)
        }
        favAdapter = FavAdapter(
            action = {
                favViewModel.deleteLocation(it)
                Toast.makeText(requireContext() , "Location Deleted" , Toast.LENGTH_SHORT).show()
            },
            sendToDetails = { lat , lon  , id->
                var intent = Intent(requireContext() , FavDetailsActivity::class.java)
                intent.putExtra("lat" , lat)
                intent.putExtra("lon" , lon)
                intent.putExtra("id" , id)
                startActivity(intent)
            }
        )
        favLayoutManager = LinearLayoutManager(requireContext() , LinearLayoutManager.VERTICAL , false)
        binding.recyclerViewFav.layoutManager = favLayoutManager
        binding.recyclerViewFav.adapter = favAdapter
        favViewModelFactory = FavouritViewModelFactory(
            AppRepoImpl.getInstance(
                AppRemoteDataSourseImpl, AppLocalDataSourseImpL.getInstance(requireContext())
        ))
        favViewModel = ViewModelProvider(this , favViewModelFactory).get(FavouritViewModel::class.java)
        favViewModel.getAllLocations()
        lifecycleScope.launch{
            favViewModel.allLocations.collectLatest {
               when(it){
                   is DbState.Loading -> {
//                       binding.progressBar.visibility = View.VISIBLE
                   }
                     is DbState.Success -> {
//                         binding.progressBar.visibility = View.GONE
                         favAdapter.submitList(it.data)
                         Log.i("dbres", "onViewCreated: " + it.data)
                     }
                   is DbState.Failure -> {

                   }
               }
            }
        }

    }


}