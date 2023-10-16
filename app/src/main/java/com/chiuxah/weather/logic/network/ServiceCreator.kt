package com.chiuxah.weather.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ServiceCreator {
    const val URL = "https://api.caiyunapp.com"
    val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(Place : Class<T>) : T = retrofit.create(Place)

    inline fun <reified  T> create() : T = create(T::class.java)
    //固定格式，在之前学过

}