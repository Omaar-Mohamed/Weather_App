package com.example.weatherapp.favourit.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentFavouritBinding


class FavouritFragment : Fragment() {
    lateinit var binding: FragmentFavouritBinding

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
    }


}