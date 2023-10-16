package com.chiuxah.weather.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.chiuxah.weather.logic.Repository
import com.chiuxah.weather.logic.model.Location

class WeatherViewModel :  ViewModel(){
    var jingdu = ""
    var weidu = ""
    var placename = ""

    private val locationLiveData = MutableLiveData<Location>()

   val weatherLiveData =
       locationLiveData.switchMap { location -> Repository.refreshWeather(location.jingdu,location.weidu) } //这里的错误

    fun refreshWeather(jingdu : String,weidu : String) {locationLiveData.value = Location(jingdu, weidu )}

}