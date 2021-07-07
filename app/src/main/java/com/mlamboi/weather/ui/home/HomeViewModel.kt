package com.mlamboi.weather.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mlamboi.weather.data.model.LocationData
import com.mlamboi.weather.data.model.Resource
import com.mlamboi.weather.data.model.WeatherInformation
import com.mlamboi.weather.data.remote.repository.WeatherRepository
import com.mlamboi.weather.utils.LocationProvider
import com.mlamboi.weather.utils.RequestCompleteListener
import kotlinx.coroutines.launch
import java.io.IOException

class HomeViewModel : ViewModel() {

    private val _locationLiveData: MutableLiveData<LocationData> = MutableLiveData()
    val locationLiveData: LiveData<LocationData> get() = _locationLiveData

    private val _locationLiveDataError: MutableLiveData<String> = MutableLiveData()
    val locationLiveDataError: LiveData<String> get() = _locationLiveDataError

    private val _weatherLiveData: MutableLiveData<Resource<WeatherInformation>> = MutableLiveData()
    val weatherLiveData: LiveData<Resource<WeatherInformation>> get() = _weatherLiveData

    /**
     * Get user's current location
     */
    fun getCurrentLocation(locationProvider: LocationProvider){
        locationProvider.getCurrentLocation(
            object : RequestCompleteListener<LocationData>{
                override fun onRequestSuccess(data: LocationData) {
                    _locationLiveData.postValue(data)
                }

                override fun onRequestFailure(message: String?) {
                    _locationLiveDataError.postValue(message)
                }
            }
        )
    }

    /**
     * Get weather information based on the user's current location
     */
    fun getWeatherInformation(
        repository: WeatherRepository,
        latitude: String,
        longitude: String
    ){
        viewModelScope.launch {
            safeWeatherInformation(repository,latitude, longitude)
        }
    }

    private suspend fun safeWeatherInformation(
        repository: WeatherRepository,
        latitude: String,
        longitude: String
    ){
        try {
            val response = repository.getWeatherInformation(latitude,longitude)
            _weatherLiveData.postValue(
                if (response.isSuccessful)
                    Resource.success(response.body())
                else
                    Resource.error(null,"${response.errorBody()}")
            )
        }
        catch (t: Throwable){
            when(t){
                is IOException -> _weatherLiveData.postValue(Resource.error(null,"Network connection failure"))
                else -> _weatherLiveData.postValue(Resource.error(null,t.localizedMessage))
            }
        }
    }
}