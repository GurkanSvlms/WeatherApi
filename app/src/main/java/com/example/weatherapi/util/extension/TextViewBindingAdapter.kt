package com.example.weatherapi.util.extension

import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter

@BindingAdapter("setWeatherDegree")
fun setWeatherDegree(textView: TextView, degree : Double?){
    degree?.let {
        textView.text = "${it.toInt()} °C"
    }
}