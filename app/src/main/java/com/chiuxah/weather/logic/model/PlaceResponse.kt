package com.chiuxah.weather.logic.model
//定义数据模型，接受搜索天气数据的JSON中的对应数据
import com.google.gson.annotations.SerializedName

data class PlaceResponse(val status : String,val places : List<Place>)

data class Place(val name : String,val location : Location,@SerializedName("formatted_address") val address : String)//搜索出来的地区名字，经纬度（由下面定义）

data class Location(@SerializedName("lng") val jingdu : String,@SerializedName("lat") val weidu : String)//经度，纬度