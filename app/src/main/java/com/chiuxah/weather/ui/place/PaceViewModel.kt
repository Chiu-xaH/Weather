package com.chiuxah.weather.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.chiuxah.weather.logic.Repository
//import com.chiuxah.weather.logic.dao.PlaceDao
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

    //统一把PlaceDao的用法引用过来，也可以随用随引，甚至可以写在一起。。。。其实上面的那个方法也是引用过来的
   // fun savePlace(place: Place) = PlaceDao.save(place)
   // fun getSavedPlace() = PlaceDao.get()
    //fun isPlaceSaved() = PlaceDao.ifSaved()
}