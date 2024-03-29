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
import com.example.weatherapp.shared.ApiConstants
import com.example.weatherapp.shared.SharedViewModel
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.navigation.NavigationView
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

        // Get the selected language from shared preferences
        val selectedLanguage = getSelectedLanguage(requireContext())
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        // Set the checked state of the language buttons based on the selected language
        when (selectedLanguage) {
            "en" -> binding.englishButton.isChecked = true
            "ar" -> binding.arabicButton.isChecked = true
        }

        // Language toggle group listener
        binding.languageToggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.englishButton -> {
                        // Save the selected language as English
                        saveSelectedLanguage(requireContext(), "en")
                        // Set the language and update the UI
                        setLanguage("en")
                        // Update drawer menu items with localized strings
                        updateDrawerMenuItems("en")
                        this.findNavController().navigate(R.id.settingsFragment)
                    }
                    R.id.arabicButton -> {
                        // Save the selected language as Arabic
                        saveSelectedLanguage(requireContext(), "ar")
                        // Set the language and update the UI
                        setLanguage(ApiConstants.getSelectedLanguage(requireContext()))
                        // Update drawer menu items with localized strings
                        updateDrawerMenuItems(ApiConstants.getSelectedLanguage(requireContext()))
                        this.findNavController().navigate(R.id.settingsFragment)
                    }
                }
            }
        }
    }

    private fun setLanguage(languageCode: String) {
        // Set the locale and update configuration to change the language
        setLocale(languageCode)
        // Notify observers (activities and fragments) about the language change
        sharedViewModel.setLanguage(languageCode)
    }

    private fun setLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)

        // Create a new configuration
        val config = Configuration()
        config.setLocale(locale)

        // Set layout direction based on the language
        if (language == "ar") {
            config.setLayoutDirection(locale)
        } else {
            // Set LTR direction for other languages
            config.setLayoutDirection(Locale("en"))
        }

        // Update the resources configuration
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    private fun updateDrawerMenuItems(languageCode: String) {
        val navView = requireActivity().findViewById<NavigationView>(R.id.nav_view)
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

    private fun getLocalizedString(resId: Int, languageCode: String): String {
        val locale = Locale(languageCode)
        val configuration = Configuration(resources.configuration)
        configuration.setLocale(locale)
        val localizedContext = requireContext().createConfigurationContext(configuration)
        return localizedContext.resources.getString(resId)
    }



    private fun saveSelectedLanguage(context: Context, languageCode: String) {
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