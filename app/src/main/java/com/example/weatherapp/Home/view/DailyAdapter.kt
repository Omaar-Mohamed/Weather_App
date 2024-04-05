package com.example.weatherapp.Home.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.databinding.DailyItemBinding
import com.example.weatherapp.databinding.HourItemBinding
import com.example.weatherapp.model.dto.Daily
import com.example.weatherapp.model.dto.Hourly
import com.example.weatherapp.shared.ApiConstants

class DailyAdapter : ListAdapter<Daily , DailyAdapter.DailyViewHolder>(DailyDiffUtil()) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = DailyItemBinding.inflate(inflater, parent, false)
            return DailyViewHolder(binding)
        }

        override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
            val daily = getItem(position)
            holder.bind(daily)
        }

        class DailyDiffUtil : DiffUtil.ItemCallback<Daily>() {
            override fun areItemsTheSame(oldItem: Daily, newItem: Daily): Boolean {
                return oldItem.dt == newItem.dt // Assuming dt is a unique identifier for Daily objects
            }

            override fun areContentsTheSame(oldItem: Daily, newItem: Daily): Boolean {
                return oldItem == newItem
            }
        }

        class DailyViewHolder(private val binding: DailyItemBinding) : RecyclerView.ViewHolder(binding.root) {
            private val txtDate: TextView = binding.textViewDay
            private val imgWeather: ImageView = binding.imageViewIcon
            private val txtWeather: TextView = binding.textViewMinTemperature
            private val txtDegree: TextView = binding.textViewMaxTemperature

            fun bind(daily: Daily) {
                txtDate.text = ApiConstants.convertUnixTimestampToDateTimeToDailyAdapter(daily.dt , ApiConstants.getSelectedLanguage(itemView.context))
                Glide.with(itemView.context)
                    .load(ApiConstants.getWeatherIconUrl(daily.weather[0].icon))
                    .into(imgWeather)
                txtWeather.text = daily.weather[0].description
                txtDegree.text = when (val selectedDegree = ApiConstants.getSelectedDegree(itemView.context)) {
                    "metric" -> String.format("%.0f°C", daily.temp.max ,"/" ,daily.temp.min)
                    "imperial" -> String.format("%.0f°F", daily.temp.max ,"/" ,daily.temp.min)
                    "standard" -> String.format("%.0f°", daily.temp.max ,"/" ,daily.temp.min)
                    else -> {
                        // Handle unknown degree types gracefully
                        "Unknown degree type: $selectedDegree"
                    }
                }

            }
        }
}