package com.example.weatherapi.ui.home.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapi.model.cityResponse.forecast.CityForecast
import com.example.weatherapi.repository.CityRepository
import com.example.weatherapi.util.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityDetailViewModel @Inject constructor(private val cityRepository: CityRepository) : ViewModel() {
    val cityResponse : MutableLiveData<CityForecast> = MutableLiveData()
    val isLoading : MutableLiveData<Boolean> = MutableLiveData()
    val onError : MutableLiveData<String?> = MutableLiveData()

    fun getCityForecast(
        woeId : String
    ) = viewModelScope.launch {
        isLoading.value = true
        val request = cityRepository.getCityForecast(woeId)
        when(request){
            is NetworkResult.Success ->{
                cityResponse.value = request.data
                isLoading.value = false
            }
            is NetworkResult.Error ->{
                onError.value = request.message
                isLoading.value = false
            }
        }
    }
}