package com.yehorlevchenko.presentation.ui.screen.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.yehorlevchenko.presentation.R
import com.yehorlevchenko.presentation.databinding.ViewCityWeatherHistoryListItemBinding
import com.yehorlevchenko.presentation.entity.CityWeatherHistoryScreenEntity

class CityWeatherHistoryAdapter
    : RecyclerView.Adapter<CityWeatherHistoryAdapter.CityWeatherHistoryViewHolder>() {

    private val differ = AsyncListDiffer(this, ItemDiffCallback())
    private var onCityWeatherHistoryItemClick: (Long) -> Unit = {}

    fun doOnCityWeatherHistoryItemClick(onCityWeatherHistoryItemClick: (Long) -> Unit) {
        this.onCityWeatherHistoryItemClick = onCityWeatherHistoryItemClick
    }

    fun setItems(items: List<CityWeatherHistoryScreenEntity>) {
        differ.submitList(items)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CityWeatherHistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewCityWeatherHistoryListItemBinding.inflate(inflater, parent, false)

        return CityWeatherHistoryViewHolder(binding, onCityWeatherHistoryItemClick)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: CityWeatherHistoryViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    class CityWeatherHistoryViewHolder(
        private val binding: ViewCityWeatherHistoryListItemBinding,
        private val onCityWeatherHistoryItemClick: (Long) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CityWeatherHistoryScreenEntity) {
            setListener(item.id)
            setDate(item.date)
            setDetails(item.description, item.temperature)
            setIcon(item.iconUrl)
        }

        private fun setListener(id: Long) {
            binding.root.setOnClickListener {
                onCityWeatherHistoryItemClick(id)
            }
        }

        private fun setDate(date: String) {
            binding.tvDate.text = date
        }

        private fun setDetails(description: String, temperature: String) {
            val resources = binding.root.resources
            val temperatureValue = resources.getString(R.string.weather_details_data_placeholder_temperature, temperature)
            val generalValue = resources.getString(R.string.placeholder_weather_description_and_temperature, description, temperatureValue)
            binding.tvDetails.text = generalValue
        }

        private fun setIcon(iconUrl: String) {
            Picasso.get().load(iconUrl).error(R.drawable.ic_weather_description_icon_placeholder).into(binding.ivIcon)
        }
    }

    inner class ItemDiffCallback : DiffUtil.ItemCallback<CityWeatherHistoryScreenEntity>() {

        override fun areItemsTheSame(
            oldItem: CityWeatherHistoryScreenEntity,
            newItem: CityWeatherHistoryScreenEntity
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CityWeatherHistoryScreenEntity,
            newItem: CityWeatherHistoryScreenEntity
        ): Boolean {
            return oldItem == newItem
        }
    }
}