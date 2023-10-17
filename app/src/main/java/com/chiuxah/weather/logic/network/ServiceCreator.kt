package com.chiuxah.weather.logic.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ServiceCreator {

    //val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mmXXX", Locale.getDefault())
    val gsonBuilder = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    val gson = gsonBuilder.create()

    const val URL = "https://api.caiyunapp.com"
    val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    fun <T> create(Place : Class<T>) : T = retrofit.create(Place)

    inline fun <reified  T> create() : T = create(T::class.java)
    //固定格式，在之前学过

}