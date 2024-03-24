package com.example.weatherapp.alert.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.AlertItemBinding
import com.example.weatherapp.model.dto.AlertDto

class AlartAdapter(private val action: (AlertDto)->Unit) :
    ListAdapter<AlertDto, AlartAdapter.AlertViewHolder>(AlertDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AlertItemBinding.inflate(inflater, parent, false)
        return AlertViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        val alert = getItem(position)
        holder.bind(alert, action)
    }

    class AlertDiffUtil : DiffUtil.ItemCallback<AlertDto>() {
        override fun areItemsTheSame(oldItem: AlertDto, newItem: AlertDto): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AlertDto, newItem: AlertDto): Boolean {
            return oldItem == newItem
        }
    }

    class AlertViewHolder(private val binding: AlertItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val txtLocation: TextView = binding.locationTextView
        private val txtDate: TextView = binding.alertDateTextView
        private val txtTime: TextView = binding.alertTimeTextView

        fun bind(alert: AlertDto, action: (AlertDto) -> Unit) {
            txtLocation.text = alert.location
            txtDate.text = alert.alertDate
            txtTime.text = alert.alertTime
            binding.root.setOnClickListener {
                action(alert)
            }
        }
    }
}