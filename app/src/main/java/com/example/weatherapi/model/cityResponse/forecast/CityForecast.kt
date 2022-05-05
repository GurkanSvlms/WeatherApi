package com.example.weatherapi.model.cityResponse.forecast


import com.google.gson.annotations.SerializedName

/*
  //GET 5 DAY FORECAST
  // https://www.metaweather.com/api/location/woeid/
*/

data class CityForecast(
    @SerializedName("consolidated_weather")
    val consolidatedWeather: List<ConsolidatedWeather>?,
    @SerializedName("latt_long")
    val latLong: String?,
    @SerializedName("location_type")
    val locationType: String?,
    @SerializedName("parent")
    val parent: Parent?,
    @SerializedName("sources")
    val sources: List<Source>?,
    @SerializedName("sun_rise")
    val sunRise: String?,
    @SerializedName("sun_set")
    val sunSet: String?,
    @SerializedName("time")
    val time: String?,
    @SerializedName("timezone")
    val timezone: String?,
    @SerializedName("timezone_name")
    val timezoneName: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("woeid")
    val woeId: Int?
)