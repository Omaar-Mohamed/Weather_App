package com.example.weatherapp.favourit.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.FavItemBinding
import com.example.weatherapp.model.dto.FavLocations

class FavAdapter(
    private val action: (FavLocations) -> Unit,
    private val sendToDetails: (Float, Float) -> Unit
) : ListAdapter<FavLocations, FavAdapter.FavViewHolder>(FavDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FavItemBinding.inflate(inflater, parent, false)
        return FavViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        val fav = getItem(position)
        holder.bind(fav, action, sendToDetails)
    }

    class FavDiffUtil : DiffUtil.ItemCallback<FavLocations>() {
        override fun areItemsTheSame(oldItem: FavLocations, newItem: FavLocations): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FavLocations, newItem: FavLocations): Boolean {
            return oldItem == newItem
        }
    }

    class FavViewHolder(private val binding: FavItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val txtAddress: TextView = binding.textViewMinTemperature
        private val removeBtn: ImageButton = binding.imageViewNewIcon

        fun bind(
            fav: FavLocations,
            action: (FavLocations) -> Unit,
            sendToDetails: (Float, Float) -> Unit
        ) {
            txtAddress.text = fav.address
            removeBtn.setOnClickListener {
                action(fav)
            }
            itemView.setOnClickListener {
                // Invoke sendToDetails with latitude and longitude
                sendToDetails(15.0f, 13.0f)
            }
        }
    }
}
