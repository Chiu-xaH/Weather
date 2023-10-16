package com.chiuxah.weather.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.chiuxah.weather.logic.Repository
import com.chiuxah.weather.logic.model.Place

class PaceViewModel : ViewModel() {
    //如果ViewModel中的某个LiveData对象是调用另外的方法获取的，那么我们就可以借助
    //switchMap()方法，将这个LiveData对象转换成另外一个可观察的LiveData对象

    val list = ArrayList<Place>()

   private val searchLIVEDATA = MutableLiveData<String>()
    val q  =
        searchLIVEDATA.switchMap { query -> Repository.searchCity(query) }
    fun getQuery(query : String) {
        searchLIVEDATA.value = query
    }
}