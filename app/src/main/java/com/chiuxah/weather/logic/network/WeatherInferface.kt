package com.chiuxah.weather.logic.network

import com.chiuxah.weather.MyApplication
import com.chiuxah.weather.logic.model.DailyResponse
import com.chiuxah.weather.logic.model.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
//类似写法
interface WeatherInferface {
    @GET("v2.6/${MyApplication.mytoken}/{jingdu},{weidu}/realtime.json")
    fun getWeather(@Path("jingdu") jingdu : String,@Path("weidu") weidu : String) : Call<RealtimeResponse>

    @GET("v2.6/${MyApplication.mytoken}/{jingdu},{weidu}/daily.json")
    fun getDailyWeather(@Path("jingdu") jingdu : String,@Path("weidu") weidu : String) : Call<DailyResponse>

}