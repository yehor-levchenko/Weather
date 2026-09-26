package com.yehorlevchenko.presentation.ui.screen.cities

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yehorlevchenko.presentation.databinding.ViewCitiesListItemBinding
import com.yehorlevchenko.presentation.entity.CityScreenEntity

class CitiesAdapter : RecyclerView.Adapter<CitiesAdapter.CitiesViewHolder>() {

    private val differ = AsyncListDiffer(this, ItemDiffCallback())
    private var onCityItemClick: (CityScreenEntity) -> Unit = {}
    private var onCityWeatherHistoryIconClick: (CityScreenEntity) -> Unit = {}

    fun doOnCityItemClick(onCityItemClick: (CityScreenEntity) -> Unit) {
        this.onCityItemClick = onCityItemClick
    }

    fun doOnCityWeatherHistoryIconClick(onCityWeatherHistoryIconClick: (CityScreenEntity) -> Unit) {
        this.onCityWeatherHistoryIconClick = onCityWeatherHistoryIconClick
    }

    fun setItems(items: List<CityScreenEntity>) {
        differ.submitList(items)
    }

    fun getItemAtPosition(position: Int): CityScreenEntity {
        return differ.currentList[position]
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CitiesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewCitiesListItemBinding.inflate(inflater, parent, false)

        return CitiesViewHolder(binding, onCityItemClick, onCityWeatherHistoryIconClick)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: CitiesViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    class CitiesViewHolder(
        private val binding: ViewCitiesListItemBinding,
        private val onCityItemClick: (CityScreenEntity) -> Unit,
        private val onCityWeatherHistoryIconClick: (CityScreenEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CityScreenEntity) {
            setListeners(item)
            setName(item.name)
        }

        private fun setListeners(item: CityScreenEntity) {
            binding.root.setOnClickListener {
                onCityItemClick(item)
            }
            binding.ivCityWeatherHistoryIcon.setOnClickListener {
                onCityWeatherHistoryIconClick(item)
            }
        }

        private fun setName(name: String) {
            binding.tvName.text = name
        }
    }

    inner class ItemDiffCallback : DiffUtil.ItemCallback<CityScreenEntity>() {

        override fun areItemsTheSame(
            oldItem: CityScreenEntity,
            newItem: CityScreenEntity
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CityScreenEntity,
            newItem: CityScreenEntity
        ): Boolean {
            return oldItem == newItem
        }
    }
}