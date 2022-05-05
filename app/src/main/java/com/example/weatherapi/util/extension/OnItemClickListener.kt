package com.example.weatherapi.util.extension

import com.example.weatherapi.model.cityResponse.city.NearbyCityItem


interface OnItemClickListener {
    fun onClick(cityDetail : NearbyCityItem)
}