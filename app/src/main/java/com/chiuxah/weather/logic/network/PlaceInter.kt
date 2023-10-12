package com.chiuxah.weather.logic.network

import android.app.Application
import com.chiuxah.weather.MyApplication
import com.chiuxah.weather.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceInterface  {
    @GET("v2/place?token=${MyApplication.mytoken}&lang=zh_CN")
    fun searchCity(@Query("query") query : String) : Call<PlaceResponse>
    //原链接API（查询天气）为 https://api.caiyunapp.com/v2/place?query=地区&token=口令&lang=zh_CN
    //@Query是用来给API添加query=的，原链接有query=，需要自行输入搜索内容，定义搜索内容类型为String
    //必须指定Call回传数据，后面是指定回传之后转换的类型
}