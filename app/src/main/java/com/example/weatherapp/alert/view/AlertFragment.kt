package com.example.weatherapp.alert.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentAlertBinding


class AlertFragment : Fragment() {

    lateinit var binding: FragmentAlertBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAlertBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
            val alertWorker = OneTimeWorkRequestBuilder<AlertWorker>()
                .build()
            WorkManager.getInstance(requireContext()).enqueue(alertWorker)
        }
    }
}