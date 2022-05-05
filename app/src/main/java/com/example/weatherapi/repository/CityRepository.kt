package com.example.weatherapi.repository

import com.example.weatherapi.network.service.ApiService
import com.example.weatherapi.util.base.BaseRepository
import javax.inject.Inject

class CityRepository @Inject constructor(private val apiService : ApiService) : BaseRepository(){
    suspend fun getNearbyCities(
        latLong : String
    ) = safeApiRequest {
        apiService.getNearbyCities(latLong)
    }

    suspend fun getCityForecast(
        woeId : String
    ) = safeApiRequest {
        apiService.getCityForecast(woeId)
    }

}