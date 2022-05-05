package com.example.weatherapi.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapi.databinding.ItemForecastRowBinding
import com.example.weatherapi.model.cityResponse.forecast.ConsolidatedWeather


class CityWeatherForecastAdapter : RecyclerView.Adapter<CityWeatherForecastAdapter.ViewHolder>() {
    private var cityWeatherForecastList : ArrayList<ConsolidatedWeather> = arrayListOf()

    class ViewHolder(private val itemBinding: ItemForecastRowBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(consolidatedWeather: ConsolidatedWeather) {
            itemBinding.consolidateWeather = consolidatedWeather
        }
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemForecastRowBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cityWeatherForecastList[position])
    }

    override fun getItemCount(): Int = cityWeatherForecastList.size

    fun updateList(newList : ArrayList<ConsolidatedWeather>){
        cityWeatherForecastList = newList
        notifyDataSetChanged()
    }
}