package com.example.weatherapi.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapi.databinding.ItemCityRowBinding
import com.example.weatherapi.model.cityResponse.city.NearbyCityItem
import com.example.weatherapi.util.extension.OnItemClickListener

class CitiesAdapter(
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<CitiesAdapter.ViewHolder>() {

    var citiesList = arrayListOf<NearbyCityItem>()

    class ViewHolder(private val itemBinding : ItemCityRowBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(listener : OnItemClickListener,cityDetail : NearbyCityItem){
            itemBinding.onItemClickListener = listener
            itemBinding.cityDetail = cityDetail
            itemBinding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemCityRowBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(onItemClickListener,citiesList[position])
    }

    override fun getItemCount(): Int = citiesList.size

    fun updateList(_citiesList : ArrayList<NearbyCityItem>){
        citiesList = _citiesList
        notifyDataSetChanged()
    }
}