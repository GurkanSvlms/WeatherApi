package com.example.weatherapi.model.errorResponse


import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("detail")
    val errorMessage: String?
)