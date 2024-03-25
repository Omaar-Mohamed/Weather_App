package com.example.weatherapp.settings.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentSettingsBinding
import com.google.android.material.button.MaterialButtonToggleGroup
import kotlinx.coroutines.launch


class SettingsFragment : Fragment() {
    lateinit var binding: FragmentSettingsBinding

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

        // Language toggle group listener
        binding.languageToggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.englishButton -> {
                        LanguageManager.setLanguage(Language.ENGLISH)
                    }
                    R.id.arabicButton -> {
                        LanguageManager.setLanguage(Language.ARABIC)
                    }
                }
            }
        }
        lifecycleScope.launch {
            LanguageManager.languageFlow.collect { language ->
                // Update UI with the new language
                when (language) {
                    Language.ENGLISH -> {
                        // Update text for English language
                        binding.languageTextView.text = getString(R.string.language)
                        // Update other UI elements for English language if needed
                    }
                    Language.ARABIC -> {
                        // Update text for Arabic language
//                        binding.languageTextView.text = getString(R.string.language_arabic)
                        // Update other UI elements for Arabic language if needed
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
}