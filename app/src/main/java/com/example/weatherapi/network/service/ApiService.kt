package com.example.weatherapi.network.service

import com.example.weatherapi.model.cityResponse.city.NearbyCity
import com.example.weatherapi.model.cityResponse.forecast.CityForecast
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/")
    suspend fun getNearbyCities(
        @Query("lattlong") latLong : String
    ) : NearbyCity

    @GET("{woeid}/")
    suspend fun getCityForecast(
        @Path("woeid")woeId : String
    ) : CityForecast
}