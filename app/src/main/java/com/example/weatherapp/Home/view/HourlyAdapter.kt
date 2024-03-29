package com.example.weatherapp.Home.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.databinding.HourItemBinding
import com.example.weatherapp.model.dto.Hourly
import com.example.weatherapp.shared.ApiConstants

class HourlyAdapter : ListAdapter<Hourly, HourlyAdapter.HourlyViewHolder>(HourlyDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = HourItemBinding.inflate(inflater, parent, false)
        return HourlyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        val hourly = getItem(position)
        holder.bind(hourly)
    }

    class HourlyDiffUtil : DiffUtil.ItemCallback<Hourly>() {
        override fun areItemsTheSame(oldItem: Hourly, newItem: Hourly): Boolean {
            return oldItem.dt == newItem.dt // Assuming dt is a unique identifier for Hourly objects
        }

        override fun areContentsTheSame(oldItem: Hourly, newItem: Hourly): Boolean {
            return oldItem == newItem
        }
    }

    class HourlyViewHolder(private val binding: HourItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val txtDate: TextView = binding.txtDate
        private val imgWeather: ImageView = binding.imgIcon
        private val txtWeather: TextView = binding.txtWeather
        private val txtDegree: TextView = binding.txtDegree

        fun bind(hourly: Hourly) {
            txtDate.text = ApiConstants.convertUnixTimestampToDateTime(hourly.dt)
            Glide.with(itemView.context)
                .load(ApiConstants.getWeatherIconUrl(hourly.weather[0].icon))
                .into(imgWeather)
            txtWeather.text = hourly.weather[0].description
                txtDegree.text = ApiConstants.kelvinToCelsiusToAdapter(hourly.temp)
        }
    }
}
