package com.chiuxah.weather.logic

import androidx.lifecycle.liveData
import com.chiuxah.weather.logic.model.PlaceResponse
import com.chiuxah.weather.logic.model.Weather
import com.chiuxah.weather.logic.network.NetWork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope


object Repository {
    //D。。。。IO为线程参数，是协程知识点

    //统一把PlaceDao的用法引用过来，也可以随用随引，甚至可以写在一起。。。。
    //fun savePlace(place: Place) = PlaceDao.save(place)
    //fun getSavedPlace() = PlaceDao.get()
   // fun isPlaceSaved() = PlaceDao.ifSaved()

    fun searchCity(query : String) = liveData(Dispatchers.IO) {
        val result = try {
            val placeResponse : PlaceResponse= NetWork.searchCities(query)
            if (placeResponse.status == "ok") {
                val p = placeResponse.places
                Result.success(p)
            } else { Result.failure(RuntimeException("placeResponse状态为 ${placeResponse.status}")) }

        } catch (e : Exception) {
            Result.failure(e)
        }
        emit(result)
    }

    fun refreshWeather(jingdu : String,weidu : String) = liveData(Dispatchers.IO) {
        val result = try {
            //协程作用域，因为要让两个请求并行，提高效率，而且async只能在协程调用,不写协程也不是不行
            coroutineScope {
                val deferredDaily = async { NetWork.getDailyWeather(jingdu,weidu) }
                val dererredRealtime = async { NetWork.getRealtimeWeather(jingdu,weidu) }

                val RealtimeResponse = dererredRealtime.await()
                val DailyResponse =deferredDaily.await()
                //相似写法
                if (RealtimeResponse.status == "ok" && DailyResponse.status == "ok") {
                    val w = Weather(RealtimeResponse.result.realtime,DailyResponse.result.daily)
                    Result.success(w)
                } else {
                    Result.failure(
                        RuntimeException("RealtimeResponse状态为 ${RealtimeResponse.status}" +
                                "DailyResponse状态为 ${DailyResponse.status}")
                    )
                }
            }
        } catch (e : Exception) {
            Result.failure<Weather>(e)
        }
        emit(result)
    }
}