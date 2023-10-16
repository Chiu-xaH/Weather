package com.chiuxah.weather.logic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object NetWork {
    private val Place = ServiceCreator.create<PlaceInterface>()
    //调用接口PlaceInterface
    //suspend是挂起函数，只能在协程中调用，适用于写耗时或者等待操作，不会堵塞主线程
    suspend fun searchCities(query: String) = Place.searchCity(query).await()
    //调用上面定义的Place中的ServiceCreator中的creator方法，传入接口，最后接口中定义的searchCity方法被调用出来
    //固定写法11.7.3
    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(
                        RuntimeException("响应对象为空")
                    )
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
//类似写法，realtime实时的
    private val Weather = ServiceCreator.create(WeatherInferface::class.java)
    suspend fun getDailyWeather(jingdu : String,weidu : String) = Weather.getDailyWeather(jingdu,weidu).await()
    suspend fun getRealtimeWeather(jingdu : String,weidu : String) = Weather.getWeather(jingdu,weidu).await()
}