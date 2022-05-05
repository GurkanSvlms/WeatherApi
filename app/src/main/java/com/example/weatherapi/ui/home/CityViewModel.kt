package com.example.weatherapi.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapi.model.cityResponse.city.NearbyCity
import com.example.weatherapi.repository.CityRepository
import com.example.weatherapi.util.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CityViewModel @Inject constructor(private val cityRepository: CityRepository): ViewModel() {
    val cityResponse : MutableLiveData<NearbyCity> = MutableLiveData()
    val isLoading : MutableLiveData<Boolean> = MutableLiveData()
    val onError : MutableLiveData<String?> = MutableLiveData()

    fun getNearbyCities(
        latLong : String
    ) = viewModelScope.launch {
        isLoading.value = true
        val request = cityRepository.getNearbyCities(latLong)
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