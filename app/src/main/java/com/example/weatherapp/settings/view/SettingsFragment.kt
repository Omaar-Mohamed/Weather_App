package com.example.weatherapp.settings.view

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentSettingsBinding
import com.example.weatherapp.shared.SharedViewModel
import com.google.android.material.button.MaterialButtonToggleGroup
import kotlinx.coroutines.launch
import java.util.Locale


class SettingsFragment : Fragment() {
    lateinit var binding: FragmentSettingsBinding
    lateinit var selectedLanguage: String // Declare the property
    lateinit var sharedViewModel: SharedViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectedLanguage = getSelectedLanguage(requireContext()) // Get the selected language from shared preferences
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        when (selectedLanguage) {
            "en" -> binding.englishButton.isChecked = true
            "ar" -> binding.arabicButton.isChecked = true
        }
        // Language toggle group listener
        binding.languageToggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.englishButton -> {
                        Toast.makeText(
                            requireContext(),
                            "Selected language: English",
                            Toast.LENGTH_SHORT
                        ).show()
                        saveSelectedLanguage(requireContext(), "en")
//                        changeLanguage(getSelectedLanguage(requireContext()))
                        sharedViewModel.setLanguage("en")
                        lifecycleScope.launch {
                            sharedViewModel.languageFlow.collect { language ->
                                setLocale(language)
                            }


                        }
//                        activity?.recreate()
                        this.findNavController().navigate(R.id.settingsFragment)


                    }

                    R.id.arabicButton -> {
                        Toast.makeText(
                            requireContext(),
                            "Selected language: Arabic",
                            Toast.LENGTH_SHORT
                        ).show()
                        saveSelectedLanguage(requireContext(), "ar")
//                        changeLanguage(getSelectedLanguage(requireContext()))
                        sharedViewModel.setLanguage("ar")
                        lifecycleScope.launch {
                            sharedViewModel.languageFlow.collect { language ->
                                setLocale(language)

                            }
                        }

                        this.findNavController().navigate(R.id.settingsFragment)

                    }
                }
            }

        }


        // Theme toggle group listener
//        binding.themeToggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
//            if (isChecked) {
//                val selectedTheme = view.findViewById<MaterialButtonToggleGroup>(checkedId).text.toString()
//                Toast.makeText(requireContext(), "Selected theme: $selectedTheme", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        // Temperature toggle group listener
//        binding.tempToggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
//            if (isChecked) {
//                val selectedTemp = view.findViewById<MaterialButtonToggleGroup>(checkedId).text.toString()
//                Toast.makeText(requireContext(), "Selected temperature: $selectedTemp", Toast.LENGTH_SHORT).show()
//            }
//        }
    }

    private fun setLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    fun saveSelectedLanguage(context: Context, languageCode: String) {
        val sharedPref = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("selectedLanguage", languageCode)
            apply()
        }
    }

    fun getSelectedLanguage(context: Context): String {
        val sharedPref = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
        return sharedPref.getString("selectedLanguage", "en") ?: "en" // Default to 'en' if not set
    }


}