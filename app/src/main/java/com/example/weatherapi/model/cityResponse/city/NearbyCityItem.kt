package com.example.weatherapi.model.cityResponse.city


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class NearbyCityItem(
    @SerializedName("distance")
    val distance: Int?,
    @SerializedName("latt_long")
    val latLong: String?,
    @SerializedName("location_type")
    val locationType: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("woeid")
    val woeId: Int?
) : Parcelable