package com.example.weatherapi.util.extension

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.weatherapi.R

@BindingAdapter("setWeatherIcon")
fun setWeatherIcon(imageView: ImageView,weatherStateAbbr : String?){
    when(weatherStateAbbr){
         "c" -> imageView.setImageResource(R.drawable.ic_c)
         "h" -> imageView.setImageResource(R.drawable.ic_h)
         "hc" -> imageView.setImageResource(R.drawable.ic_hc)
         "hr" -> imageView.setImageResource(R.drawable.ic_hr)
         "lc" -> imageView.setImageResource(R.drawable.ic_lc)
         "lr" -> imageView.setImageResource(R.drawable.ic_lr)
         "s" -> imageView.setImageResource(R.drawable.ic_s)
         "sl" -> imageView.setImageResource(R.drawable.ic_sl)
         "sn" -> imageView.setImageResource(R.drawable.ic_sn)
         "t" -> imageView.setImageResource(R.drawable.ic_t)
    }
}